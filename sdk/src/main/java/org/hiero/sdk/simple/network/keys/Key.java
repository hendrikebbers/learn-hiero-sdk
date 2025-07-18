package org.hiero.sdk.simple.network.keys;

import org.jspecify.annotations.NonNull;

/**
 * Represents a cryptographic key used in the Hiero network.
 */
public interface Key {

    /**
     * Converts the key to a byte array using the specified encoding.
     *
     * @param encoding the encoding to use
     * @return the byte array representation of the key
     */
    @NonNull
    byte[] toBytes(@NonNull KeyEncoding encoding);

    /**
     * Returns the algorithm used by this key.
     *
     * @return the key algorithm
     */
    @NonNull
    KeyAlgorithm algorithm();
}
