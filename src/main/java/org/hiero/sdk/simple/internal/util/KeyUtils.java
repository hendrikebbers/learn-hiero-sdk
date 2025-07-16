package org.hiero.sdk.simple.internal.util;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.proto.Key;
import com.hedera.hashgraph.sdk.proto.SignaturePair;

public class KeyUtils {

    public static SignaturePair toSignaturePairProtobuf(PublicKey publicKey, byte[] signature) {
        if (publicKey.isECDSA()) {
            return SignaturePair.newBuilder()
                    .setPubKeyPrefix(ByteString.copyFrom(publicKey.toBytesRaw()))
                    .setECDSASecp256K1(ByteString.copyFrom(signature))
                    .build();
        } else {
            return SignaturePair.newBuilder()
                    .setPubKeyPrefix(ByteString.copyFrom(publicKey.toBytesRaw()))
                    .setEd25519(ByteString.copyFrom(signature))
                    .build();
        }
    }

    public static Key toKeyProtobuf(com.hedera.hashgraph.sdk.Key key) {
        if (key instanceof PublicKey publicKey) {
            if (publicKey.isECDSA()) {
                return com.hedera.hashgraph.sdk.proto.Key.newBuilder()
                        .setECDSASecp256K1(ByteString.copyFrom(publicKey.toBytesRaw()))
                        .build();
            } else {
                return com.hedera.hashgraph.sdk.proto.Key.newBuilder()
                        .setEd25519(ByteString.copyFrom(publicKey.toBytesRaw()))
                        .build();
            }
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + key.getClass().getName());
        }
    }

}
