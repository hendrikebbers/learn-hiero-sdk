package org.hiero.sdk.simple.internal.network.key;

import java.io.IOException;
import java.util.Objects;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.KeyEncoding;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.jspecify.annotations.NonNull;

public final class KeyFactory {

    private KeyFactory() {
    }

    @NonNull
    public static PrivateKey createPrivateKey(@NonNull final KeyAlgorithm algorithm) {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        if (algorithm == KeyAlgorithm.ED25519) {
            return KeyUtilitiesED25519.createPrivateKey();
        } else if (algorithm == KeyAlgorithm.ECDSA) {
            return KeyUtilitiesECDSA.createPrivateKey();
        } else {
            throw new IllegalArgumentException("Unsupported signature algorithm: " + algorithm);
        }
    }

    @NonNull
    public static PrivateKey createPrivateKey(String privateKey) {
        final byte[] privateKeyBytes = decodeHex(privateKey);
        return createPrivateKey(privateKeyBytes);
    }

    @NonNull
    public static PrivateKey createPrivateKey(@NonNull final byte[] privateKey) {
        //TODO: can we remove this if to make the code cleaner?
        if ((privateKey.length == Ed25519.SECRET_KEY_SIZE)
                || (privateKey.length == Ed25519.SECRET_KEY_SIZE + Ed25519.PUBLIC_KEY_SIZE)) {
            return KeyUtilitiesED25519.createPrivateKeyFromBytes(privateKey);
        }
        try {
            final PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKey);
            if (privateKeyInfo.getPrivateKeyAlgorithm()
                    .equals(new AlgorithmIdentifier(KeyUtilitiesED25519.ID_ED25519))) {
                return KeyUtilitiesED25519.createPrivateKeyFromPrivateKeyInfo(privateKeyInfo);
            } else {
                return KeyUtilitiesECDSA.createPrivateKeyFromPrivateKeyInfo(privateKeyInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating private key from key info", e);
        } catch (ClassCastException | IllegalArgumentException e) {
            return new PrivateKeyWithECDSA(ECPrivateKey.getInstance(privateKey).getKey());
        }
    }

    @NonNull
    public static PrivateKey createPrivateKey(final java.security.@NonNull PrivateKey privateKey) {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        final byte[] encoded = privateKey.getEncoded();
        if (encoded == null) {
            throw new IllegalArgumentException("Given key does not support encoding");
        }
        return createPrivateKey(encoded);
    }

    public static PublicKey createPublicKey(String publicKey) {
        final byte[] publicKeyBytes = decodeHex(publicKey);
        return createPublicKey(publicKeyBytes);
    }

    @NonNull
    public static PublicKey createPublicKey(@NonNull final byte[] publicKey) {
        throw new UnsupportedOperationException("PublicKeyFactory.create is not implemented yet");
    }

    public static PublicKey createPublicKey(final java.security.@NonNull PublicKey publicKey) {
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        final byte[] encoded = publicKey.getEncoded();
        if (encoded == null) {
            throw new IllegalArgumentException("Given key does not support encoding");
        }
        return createPublicKey(encoded);
    }

    @NonNull
    private static byte[] decodeHex(@NonNull final String hex) {
        Objects.requireNonNull(hex, "hex must not be null");
        return Hex.decode(hex.startsWith("0x") ? hex.substring(2) : hex);
    }

    public static java.security.@NonNull PrivateKey toJavaPrivateKey(@NonNull PrivateKey privateKey,
            @NonNull KeyEncoding encoding) {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        Objects.requireNonNull(encoding, "encoding must not be null");
        return new java.security.PrivateKey() {
            @Override
            public String getAlgorithm() {
                return privateKey.algorithm().name();
            }

            @Override
            public String getFormat() {
                return encoding.name();
            }

            @Override
            public byte[] getEncoded() {
                return privateKey.toBytes(encoding);
            }

            @Override
            public boolean equals(Object obj) {
                return privateKey.equals(obj);
            }

            @Override
            public int hashCode() {
                return privateKey.hashCode();
            }
        };
    }

    public static java.security.@NonNull PublicKey toJavaPublicKey(@NonNull PublicKey publicKey,
            @NonNull KeyEncoding encoding) {
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        Objects.requireNonNull(encoding, "encoding must not be null");
        return new java.security.PublicKey() {
            @Override
            public String getAlgorithm() {
                return publicKey.algorithm().name();
            }

            @Override
            public String getFormat() {
                return encoding.name();
            }

            @Override
            public byte[] getEncoded() {
                return publicKey.toBytes(encoding);
            }

            @Override
            public boolean equals(Object obj) {
                return publicKey.equals(obj);
            }

            @Override
            public int hashCode() {
                return publicKey.hashCode();
            }
        };
    }
}
