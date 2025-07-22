package org.hiero.sdk.simple.network.keys;

import org.hiero.sdk.simple.internal.network.key.KeyFactory;
import org.jspecify.annotations.NonNull;

/**
 * Represents a public key in the Hiero network.
 */
public interface PublicKey extends Key {

    /**
     * Verifies a signature against a message using this public key.
     *
     * @param message   the original message
     * @param signature the signature to verify
     * @return true if the signature is valid for the message, false otherwise
     */
    boolean verify(@NonNull byte[] message, @NonNull byte[] signature);

    default java.security.@NonNull PublicKey toJavaKey(@NonNull final KeyEncoding encoding) {
        return KeyFactory.toJavaPublicKey(this, encoding);
    }

    static PublicKey from(final java.security.@NonNull PublicKey publicKey) {
        return KeyFactory.createPublicKey(publicKey);
    }

    //TODO: Do we really want to have that method without the definition of the KeyAlgorithm and KeyEncoding?
    @NonNull
    static PublicKey from(@NonNull final String publicKey) {
        return KeyFactory.createPublicKey(publicKey);
    }

    //TODO: Do we really want to have that method without the definition of the KeyAlgorithm and KeyEncoding?
    @NonNull
    static PublicKey from(@NonNull byte[] publicKey) {
        return KeyFactory.createPublicKey(publicKey);
    }
}
