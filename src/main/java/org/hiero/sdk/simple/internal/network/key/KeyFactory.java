package org.hiero.sdk.simple.internal.network.key;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.jspecify.annotations.NonNull;

public class KeyFactory {

    private KeyFactory() {
        // Prevent instantiation
    }

    public static PrivateKey createPrivateKey(KeyAlgorithm algorithm) {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        if (algorithm == KeyAlgorithm.ED25519) {
            return KeyFactoryWithED25519.createPrivateKey();
        } else if (algorithm == KeyAlgorithm.ECDSA) {
            return KeyFactoryWithECDSA.createPrivateKey();
        } else {
            throw new IllegalArgumentException("Unsupported signature algorithm: " + algorithm);
        }
    }

    public static PrivateKey createPrivateKeyFromString(String privateKey) {
        return createPrivateKeyFromBytes(
                Hex.decode(privateKey.startsWith("0x") ? privateKey.substring(2) : privateKey));
    }

    public static PrivateKey createPrivateKeyFromBytes(@NonNull final byte[] privateKey) {
        if ((privateKey.length == Ed25519.SECRET_KEY_SIZE)
                || (privateKey.length == Ed25519.SECRET_KEY_SIZE + Ed25519.PUBLIC_KEY_SIZE)) {
            // If this is a 32 or 64 byte string, assume an Ed25519 private key
            return new PrivateKeyWithED25519(Arrays.copyOfRange(privateKey, 0, Ed25519.SECRET_KEY_SIZE), null);
        }
        // Assume a DER-encoded private key descriptor
        return createPrivateKeyFromBytesDER(privateKey);
    }

    public static PrivateKey createPrivateKeyFromBytesDER(byte[] privateKey) {
        try {
            return createPrivateKeyFromPrivateKeyInfo(PrivateKeyInfo.getInstance(privateKey));
        } catch (ClassCastException | IllegalArgumentException e) {
            return new PrivateKeyWithECDSA(ECPrivateKey.getInstance(privateKey).getKey(), null);
        }
    }

    private static PrivateKey createPrivateKeyFromPrivateKeyInfo(PrivateKeyInfo privateKeyInfo) {
        if (privateKeyInfo.getPrivateKeyAlgorithm().equals(new AlgorithmIdentifier(KeyAlgorithmUtils.ID_ED25519))) {
            try {
                var privateKey = (ASN1OctetString) privateKeyInfo.parsePrivateKey();
                return new PrivateKeyWithED25519(privateKey.getOctets(), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // assume ECDSA
            try {
                var privateKey = ECPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
                return new PrivateKeyWithECDSA(privateKey.getKey(), null);
            } catch (IllegalArgumentException e) {
                // Try legacy import
                try {
                    var privateKey = (ASN1OctetString) privateKeyInfo.parsePrivateKey();
                    return new PrivateKeyWithECDSA(new BigInteger(1, privateKey.getOctets()), null);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
