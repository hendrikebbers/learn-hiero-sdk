package org.hiero.sdk.simple.internal.network.key;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.KeyEncoding;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.jspecify.annotations.NonNull;

public record PublicKeyWithED25519(byte[] keyData) implements PublicKey {

    public PublicKeyWithED25519 {
        keyData = Arrays.copyOf(keyData, keyData.length);
    }

    @Override
    public boolean verify(byte[] message, byte[] signature) {
        return Ed25519.verify(signature, 0, keyData, 0, message, 0, message.length);
    }

    @Override
    public @NonNull byte[] toBytes(@NonNull KeyEncoding encoding) {
        Objects.requireNonNull(encoding, "encoding must not be null");
        if (encoding == KeyEncoding.DER) {
            try {
                return new SubjectPublicKeyInfo(new AlgorithmIdentifier(KeyAlgorithmUtils.ID_ED25519),
                        keyData).getEncoded(
                        "DER");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (encoding == KeyEncoding.RAW) {
            return Arrays.copyOf(keyData, keyData.length);
        }
        throw new IllegalArgumentException("Unsupported key encoding: " + encoding);
    }

    @Override
    public KeyAlgorithm algorithm() {
        return KeyAlgorithm.ED25519;
    }
}
