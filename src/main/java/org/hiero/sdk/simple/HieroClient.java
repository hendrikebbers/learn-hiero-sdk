package org.hiero.sdk.simple;

import org.hiero.sdk.simple.grpc.GrpcClient;
import org.hiero.sdk.simple.internal.HieroClientImpl;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.TransactionId;
import org.hiero.sdk.simple.network.settings.NetworkSettings;
import org.jspecify.annotations.NonNull;

public interface HieroClient {

    default boolean signAutomaticallyWithOperator() {
        return true;
    }

    @NonNull
    TransactionId generateTransactionId();

    @NonNull
    GrpcClient getGrpcClient();

    Account getOperatorAccount();

    NetworkSettings getNetworkSettings();

    static HieroClient create(@NonNull Account operatorAccount, @NonNull NetworkSettings networkSettings) {
        return new HieroClientImpl(operatorAccount, networkSettings);
    }

    static HieroClient create(@NonNull Account operatorAccount, @NonNull String networkIdentifier) {
        return new HieroClientImpl(operatorAccount, NetworkSettings.forIdentifier(networkIdentifier)
                .orElseThrow(() -> new IllegalArgumentException("Invalid network identifier: " + networkIdentifier)));
    }

}
