package org.hiero.sdk.simple.network.keys;

import org.hiero.sdk.simple.internal.network.key.KeyFactory;

public interface PrivateKey extends Key {
    
    PublicKey createPublicKey();

    byte[] sign(byte[] message);

    static PrivateKey generate(KeyAlgorithm algorithm) {
        return KeyFactory.createPrivateKey(algorithm);
    }

    static PrivateKey fromString(String privateKey) {
        return KeyFactory.createPrivateKeyFromString(privateKey);
    }
}
