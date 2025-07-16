package org.hiero.sdk.simple.network.keys;

public interface Key {

    byte[] toBytes();

    boolean isAlgorithm(SignatureAlgorithm algorithm);
}
