package org.hiero.sdk.simple.internal.util;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.PublicKey;
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

}
