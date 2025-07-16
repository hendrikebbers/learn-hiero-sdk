package org.hiero.sdk.simple.network.keys;

public interface Key {

    byte[] toBytes();

    byte[] toBytesRaw();

    byte[] toBytesDER();

    SignatureAlgorithm algorithm();
}
