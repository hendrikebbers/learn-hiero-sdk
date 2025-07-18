package org.hiero.sdk.simple.network.keys;

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
    boolean verify(byte[] message, byte[] signature);

}
