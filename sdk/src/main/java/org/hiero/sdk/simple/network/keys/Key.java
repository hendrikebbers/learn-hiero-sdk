package org.hiero.sdk.simple.network.keys;

import org.jspecify.annotations.NonNull;

public interface Key {

    @NonNull
    byte[] toBytes(@NonNull KeyEncoding encoding);

    @NonNull
    KeyAlgorithm algorithm();
}
