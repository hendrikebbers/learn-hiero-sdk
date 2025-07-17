package org.hiero.sdk.simple.internal.network.key;


import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.KeyEncoding;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.jspecify.annotations.NonNull;

public record PublicKeyWithECDSA(byte[] keyData) implements PublicKey {

    public PublicKeyWithECDSA {
        keyData = Arrays.copyOf(keyData, keyData.length);
    }

    @Override
    public boolean verify(byte[] message, byte[] signature) {
        var hash = KeyAlgorithmUtils.calcKeccak256(message);
        final ECDSASigner signer = new ECDSASigner();
        signer.init(
                false,
                new ECPublicKeyParameters(
                        KeyAlgorithmUtils.ECDSA_SECP256K1_CURVE.getCurve().decodePoint(keyData),
                        KeyAlgorithmUtils.ECDSA_SECP256K1_DOMAIN));
        final BigInteger r = new BigInteger(1, Arrays.copyOf(signature, 32));
        final BigInteger s = new BigInteger(1, Arrays.copyOfRange(signature, 32, 64));
        return signer.verifySignature(hash, r, s);
    }

    @Override
    public @NonNull byte[] toBytes(@NonNull KeyEncoding encoding) {
        Objects.requireNonNull(encoding, "encoding must not be null");
        if (encoding == KeyEncoding.DER) {
            try {
                return new SubjectPublicKeyInfo(
                        new AlgorithmIdentifier(KeyAlgorithmUtils.ID_EC_PUBLIC_KEY,
                                KeyAlgorithmUtils.ID_ECDSA_SECP256K1),
                        keyData)
                        .getEncoded("DER");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (encoding == KeyEncoding.RAW) {
            return Arrays.copyOf(keyData, keyData.length);
        }
        throw new IllegalArgumentException("Unsupported key encoding: " + encoding);
    }

    @Override
    public KeyAlgorithm algorithm() {
        return KeyAlgorithm.ECDSA;
    }
}
