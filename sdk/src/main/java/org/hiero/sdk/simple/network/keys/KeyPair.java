package org.hiero.sdk.simple.network.keys;

import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record KeyPair(@NonNull PrivateKey privateKey, @NonNull PublicKey publicKey) {

    public KeyPair {
        Objects.requireNonNull(privateKey, "Private key cannot be null");
        Objects.requireNonNull(publicKey, "Public key cannot be null");
    }


    @NonNull
    public static KeyPair of(@NonNull final PrivateKey privateKey) {
        Objects.requireNonNull(privateKey, "Private key cannot be null");
        return new KeyPair(privateKey, privateKey.createPublicKey());
    }

    @NonNull
    public static KeyPair generate(@NonNull final KeyAlgorithm algorithm) {
        return of(PrivateKey.generate(algorithm));
    }
}
