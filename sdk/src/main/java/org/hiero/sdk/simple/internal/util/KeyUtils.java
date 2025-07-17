package org.hiero.sdk.simple.internal.util;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.SignaturePair;
import org.hiero.sdk.simple.network.keys.Key;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.KeyEncoding;
import org.hiero.sdk.simple.network.keys.PublicKey;

public class KeyUtils {

    public static SignaturePair toSignaturePairProtobuf(PublicKey publicKey, byte[] signature) {
        if (publicKey.algorithm() == KeyAlgorithm.ECDSA) {
            return SignaturePair.newBuilder()
                    .setPubKeyPrefix(ByteString.copyFrom(publicKey.toBytes(KeyEncoding.RAW)))
                    .setECDSASecp256K1(ByteString.copyFrom(signature))
                    .build();
        } else {
            return SignaturePair.newBuilder()
                    .setPubKeyPrefix(ByteString.copyFrom(publicKey.toBytes(KeyEncoding.RAW)))
                    .setEd25519(ByteString.copyFrom(signature))
                    .build();
        }
    }

    public static com.hedera.hashgraph.sdk.proto.Key toKeyProtobuf(Key key) {
        if (key instanceof PublicKey publicKey) {
            if (publicKey.algorithm() == KeyAlgorithm.ECDSA) {
                return com.hedera.hashgraph.sdk.proto.Key.newBuilder()
                        .setECDSASecp256K1(ByteString.copyFrom(publicKey.toBytes(KeyEncoding.RAW)))
                        .build();
            } else {
                return com.hedera.hashgraph.sdk.proto.Key.newBuilder()
                        .setEd25519(ByteString.copyFrom(publicKey.toBytes(KeyEncoding.RAW)))
                        .build();
            }
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + key.getClass().getName());
        }
    }

}
