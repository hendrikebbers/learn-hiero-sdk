package org.hiero.sdk.simple.internal.network.key;

import static org.hiero.sdk.simple.internal.network.key.KeyAlgorithmUtils.ECDSA_SECP256K1_DOMAIN;
import static org.hiero.sdk.simple.internal.network.key.KeyAlgorithmUtils.ID_ECDSA_SECP256K1;
import static org.hiero.sdk.simple.internal.network.key.KeyAlgorithmUtils.bigIntTo32Bytes;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.util.Arrays;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.KeyEncoding;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record PrivateKeyWithECDSA(BigInteger keyData, @Nullable KeyParameter chainCode) implements PrivateKey {

    @Override
    public PublicKey createPublicKey() {
        var q = ECDSA_SECP256K1_DOMAIN.getG().multiply(keyData);
        var publicParams = new ECPublicKeyParameters(q, ECDSA_SECP256K1_DOMAIN);
        return KeyFactoryWithECDSA.createPublicKey(publicParams.getQ().getEncoded(true));
    }

    @Override
    public byte[] sign(byte[] message) {
        var hash = KeyAlgorithmUtils.calcKeccak256(message);
        var signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        signer.init(true, new ECPrivateKeyParameters(keyData, ECDSA_SECP256K1_DOMAIN));
        BigInteger[] bigSig = signer.generateSignature(hash);
        byte[] sigBytes = Arrays.copyOf(bigIntTo32Bytes(bigSig[0]), 64);
        System.arraycopy(bigIntTo32Bytes(bigSig[1]), 0, sigBytes, 32, 32);
        return sigBytes;
    }

    @Override
    public @NonNull byte[] toBytes(@NonNull KeyEncoding encoding) {
        Objects.requireNonNull(encoding, "encoding must not be null");
        if (encoding == KeyEncoding.DER) {
            try {
                return new ECPrivateKey(
                        256,
                        keyData,
                        new DERBitString(createPublicKey().toBytes(KeyEncoding.RAW)),
                        new X962Parameters(ID_ECDSA_SECP256K1))
                        .getEncoded("DER");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (encoding == KeyEncoding.RAW) {
            return bigIntTo32Bytes(keyData);
        }
        throw new IllegalArgumentException("Unsupported key encoding: " + encoding);
    }


    @Override
    public KeyAlgorithm algorithm() {
        return KeyAlgorithm.ECDSA;
    }
}
