package org.hiero.sdk.simple;

import org.hiero.sdk.simple.grpc.GrpcClient;
import org.hiero.sdk.simple.internal.HieroClientImpl;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.TransactionId;
import org.hiero.sdk.simple.network.settings.NetworkSettings;
import org.jspecify.annotations.NonNull;

public interface HieroClient {

    default boolean signTransactionsAutomaticallyWithOperator() {
        return true;
    }

    @NonNull
    TransactionId generateTransactionId();

    @NonNull
    GrpcClient getGrpcClient();

    @NonNull
    Account getOperatorAccount();

    @NonNull
    NetworkSettings getNetworkSettings();

    @NonNull
    static HieroClient create(@NonNull final Account operatorAccount, @NonNull final NetworkSettings networkSettings) {
        return new HieroClientImpl(operatorAccount, networkSettings);
    }

    @NonNull
    static HieroClient create(@NonNull final Account operatorAccount, @NonNull final String networkIdentifier) {
        final NetworkSettings networkSettings = NetworkSettings.forIdentifier(networkIdentifier)
                .orElseThrow(() -> new IllegalArgumentException("Invalid network identifier: " + networkIdentifier));
        return new HieroClientImpl(operatorAccount, networkSettings);
    }

    default long getDefaultTimeoutInMs() {
        return 30_000; // 30 seconds
    }
}
