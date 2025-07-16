package org.hiero.sdk.simple.internal.network.key;

import java.io.IOException;
import java.util.Arrays;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.hiero.sdk.simple.network.keys.SignatureAlgorithm;

public record PrivateKeyWithED25519(byte[] keyData, @Nullable KeyParameter chainCode) implements PrivateKey {

    public PrivateKeyWithED25519 {
        keyData = Arrays.copyOf(keyData, keyData.length);
    }

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

    @Override
    public byte[] toBytes() {
        return toBytesRaw();
    }

    @Override
    public byte[] toBytesRaw() {
        return Arrays.copyOf(keyData, keyData.length);
    }

    @Override
    public byte[] toBytesDER() {
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(KeyAlgorithmUtils.ID_ED25519),
                    new DEROctetString(keyData))
                    .getEncoded("DER");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignatureAlgorithm algorithm() {
        return SignatureAlgorithm.ED25519;
    }
}
