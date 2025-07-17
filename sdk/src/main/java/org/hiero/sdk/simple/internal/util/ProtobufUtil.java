package org.hiero.sdk.simple.internal.util;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.AccountID;
import com.hedera.hashgraph.sdk.proto.Timestamp;
import com.hedera.hashgraph.sdk.proto.TimestampSeconds;
import com.hedera.hashgraph.sdk.proto.TransactionID;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import org.hiero.sdk.simple.network.AccountId;
import org.hiero.sdk.simple.network.TransactionId;
import org.jspecify.annotations.NonNull;

public class ProtobufUtil {

    public static TransactionID toProtobuf(@NonNull TransactionId transactionId) {
        var id = TransactionID.newBuilder();

        if (transactionId.accountId() != null) {
            id.setAccountID(toProtobuf(transactionId.accountId()));
        }

        if (transactionId.validStart() != null) {
            id.setTransactionValidStart(toProtobuf(transactionId.validStart()));
        }

        return id.build();
    }

    public static AccountID toProtobuf(@NonNull AccountId accountId) {
        var accountIdBuilder = AccountID.newBuilder()
                .setShardNum(accountId.shard())
                .setRealmNum(accountId.realm());
        if (accountId.aliasKey() != null) {
            //TODO: Bad implemented in SDK
            // accountIdBuilder.setAlias(accountId.aliasKey.toProtobufKey().toByteString());
        } else if (accountId.evmAddress() != null) {
            accountIdBuilder.setAlias(ByteString.copyFrom(accountId.evmAddress().bytes()));
        } else {
            accountIdBuilder.setAccountNum(accountId.num());
        }
        return accountIdBuilder.build();
    }


    @NonNull
    public static Duration fromProtobuf(com.hedera.hashgraph.sdk.proto.@NonNull Duration duration) {
        Objects.requireNonNull(duration, "duration must not be null");
        return Duration.ofSeconds(duration.getSeconds());
    }

    public static com.hedera.hashgraph.sdk.proto.@NonNull Duration toProtobuf(@NonNull Duration duration) {
        Objects.requireNonNull(duration, "duration must not be null");
        return com.hedera.hashgraph.sdk.proto.Duration.newBuilder()
                .setSeconds(duration.getSeconds())
                .build();
    }

    public static Instant fromProtobuf(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    /**
     * Create an instance from a timestamp in seconds protobuf.
     *
     * @param timestampSeconds the protobuf
     * @return the instance
     */
    public static Instant fromProtobuf(TimestampSeconds timestampSeconds) {
        return Instant.ofEpochSecond(timestampSeconds.getSeconds());
    }

    /**
     * Convert an instance into a timestamp.
     *
     * @param instant the instance
     * @return the timestamp
     */
    public static Timestamp toProtobuf(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    /**
     * Convert an instance into a timestamp in seconds.
     *
     * @param instant the instance
     * @return the timestamp in seconds
     */
    public static TimestampSeconds toSecondsProtobuf(Instant instant) {
        return TimestampSeconds.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .build();
    }
}
