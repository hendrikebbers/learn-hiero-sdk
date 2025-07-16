package org.hiero.sdk.simple.internal.network.key;

import javax.annotation.Nullable;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;

public record PrivateKeyWithED25519(byte[] keyData, @Nullable KeyParameter chainCode) implements PrivateKey {

    @Override
    public PublicKey createPublicKey() {
        byte[] publicKeyData = new byte[Ed25519.PUBLIC_KEY_SIZE];
        Ed25519.generatePublicKey(keyData, 0, publicKeyData, 0);
        return KeyFactoryWithED25519.createPublicKey(publicKeyData);
    }

    @Override
    public byte[] sign(byte[] message) {
        byte[] signature = new byte[Ed25519.SIGNATURE_SIZE];
        Ed25519.sign(keyData, 0, message, 0, message.length, signature, 0);
        return signature;
    }
}
