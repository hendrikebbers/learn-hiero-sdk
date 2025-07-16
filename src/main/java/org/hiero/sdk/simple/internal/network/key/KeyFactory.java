package org.hiero.sdk.simple.internal.network.key;

import java.util.Objects;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.SignatureAlgorithm;

public class KeyFactory {

    private KeyFactory() {
        // Prevent instantiation
    }

    public static PrivateKey createPrivateKey(SignatureAlgorithm algorithm) {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        if (algorithm == SignatureAlgorithm.ED25519) {
            return KeyFactoryWithED25519.createPrivateKey();
        } else if (algorithm == SignatureAlgorithm.ECDSA) {
            return KeyFactoryWithECDSA.createPrivateKey();
        } else {
            throw new IllegalArgumentException("Unsupported signature algorithm: " + algorithm);
        }
    }


}
