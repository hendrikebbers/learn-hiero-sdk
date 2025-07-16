package org.hiero.sdk.simple.network.keys;

import org.hiero.sdk.simple.internal.network.key.KeyFactory;

public interface PrivateKey {

    PublicKey createPublicKey();

    byte[] sign(byte[] message);

    static PrivateKey generate(SignatureAlgorithm algorithm) {
        return KeyFactory.createPrivateKey(algorithm);
    }
}
