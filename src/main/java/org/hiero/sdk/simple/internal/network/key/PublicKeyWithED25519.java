package org.hiero.sdk.simple.internal.network.key;

import java.util.Arrays;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.hiero.sdk.simple.network.keys.SignatureAlgorithm;

public record PublicKeyWithED25519(byte[] keyData) implements PublicKey {

    @Override
    public boolean verify(byte[] message, byte[] signature) {
        return Ed25519.verify(signature, 0, keyData, 0, message, 0, message.length);
    }

    @Override
    public byte[] toBytes() {
        return Arrays.copyOf(keyData, keyData.length);
    }

    @Override
    public boolean isAlgorithm(SignatureAlgorithm algorithm) {
        if (algorithm == SignatureAlgorithm.ED25519) {
            return true;
        }
        return false;
    }
}
