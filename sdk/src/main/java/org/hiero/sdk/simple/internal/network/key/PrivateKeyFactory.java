package org.hiero.sdk.simple.internal.network.key;

import java.io.IOException;
import java.util.Objects;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.jspecify.annotations.NonNull;

public class PrivateKeyFactory {

    private PrivateKeyFactory() {
        // Prevent instantiation
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
    public static PrivateKey createPrivate(@NonNull final String privateKeyString) {
        final byte[] privateKey = Hex.decode(
                privateKeyString.startsWith("0x") ? privateKeyString.substring(2) : privateKeyString);
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
}
