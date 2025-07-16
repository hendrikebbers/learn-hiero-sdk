package org.hiero.sdk.simple;

import com.hedera.hashgraph.sdk.TransactionId;
import org.hiero.sdk.simple.internal.HieroClientImpl;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.NetworkSettings;
import org.jspecify.annotations.NonNull;

public interface HieroClient {

    @NonNull
    TransactionId generateTransactionId();

    @NonNull
    GrpcClient getGrpcClient();

    static HieroClient create(@NonNull Account operatorAccount, @NonNull NetworkSettings networkSettings) {
        return new HieroClientImpl(operatorAccount, networkSettings);
    }

    static HieroClient create(@NonNull Account operatorAccount, @NonNull String networkIdentifier) {
        return new HieroClientImpl(operatorAccount, NetworkSettings.forIdentifier(networkIdentifier)
                .orElseThrow(() -> new IllegalArgumentException("Invalid network identifier: " + networkIdentifier)));
    }

}
