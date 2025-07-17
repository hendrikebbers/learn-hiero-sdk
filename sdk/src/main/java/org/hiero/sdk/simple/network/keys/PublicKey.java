package org.hiero.sdk.simple.network.keys;

public interface PublicKey extends Key {

    boolean verify(byte[] message, byte[] signature);

}
