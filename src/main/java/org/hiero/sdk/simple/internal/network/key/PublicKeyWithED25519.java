package org.hiero.sdk.simple.internal.network.key;

import java.io.IOException;
import java.util.Arrays;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.hiero.sdk.simple.network.keys.SignatureAlgorithm;

public record PublicKeyWithED25519(byte[] keyData) implements PublicKey {

    public PublicKeyWithED25519 {
        keyData = Arrays.copyOf(keyData, keyData.length);
    }

    @Override
    public boolean verify(byte[] message, byte[] signature) {
        return Ed25519.verify(signature, 0, keyData, 0, message, 0, message.length);
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
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(KeyAlgorithmUtils.ID_ED25519), keyData).getEncoded(
                    "DER");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignatureAlgorithm algorithm() {
        return SignatureAlgorithm.ED25519;
    }
}
