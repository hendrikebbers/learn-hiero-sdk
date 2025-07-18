package org.hiero.sdk.simple.network.keys;

import org.hiero.sdk.simple.internal.network.key.KeyFactory;

/**
 * Represents a private cryptographic key used in the Hiero network.
 */
public interface PrivateKey extends Key {

    /**
     * Creates a public key from this private key.
     *
     * @return the public key
     */
    PublicKey createPublicKey();

    /**
     * Signs a message using this private key.
     *
     * @param message the message to sign
     * @return the signature of the message
     */
    byte[] sign(byte[] message);

    /**
     * Generates a new private key using the specified algorithm.
     *
     * @param algorithm the key algorithm to use
     * @return a new private key
     */
    static PrivateKey generate(KeyAlgorithm algorithm) {
        return KeyFactory.createPrivateKey(algorithm);
    }

    //TODO: Do we really want to have that method without the definition of the KeyAlgorithm and KeyEncoding?

    /**
     * Creates a private key from a string representation.
     *
     * @param privateKey the string representation of the private key
     * @return the private key
     */
    static PrivateKey fromString(String privateKey) {
        return KeyFactory.createPrivateKeyFromString(privateKey);
    }
}
