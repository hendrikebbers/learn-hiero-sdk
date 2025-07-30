package org.hiero.sdk.simple.internal;

import java.util.Objects;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.grpc.GrpcClient;
import org.hiero.sdk.simple.internal.grpc.GrpcClientImpl;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.TransactionId;
import org.hiero.sdk.simple.network.settings.NetworkSettings;
import org.jspecify.annotations.NonNull;

public final class HieroClientImpl implements HieroClient {

    private final Account operatorAccount;

    private final NetworkSettings networkSettings;

    public HieroClientImpl(@NonNull final Account operatorAccount, @NonNull final NetworkSettings networkSettings) {
        this.operatorAccount = Objects.requireNonNull(operatorAccount, "operatorAccount must not be null");
        this.networkSettings = Objects.requireNonNull(networkSettings, "networkSettings must not be null");
    }

    @Override
    public @NonNull TransactionId generateTransactionId() {
        return TransactionId.generate(operatorAccount.accountId());
    }

    @Override
    public @NonNull GrpcClient getGrpcClient() {
        return new GrpcClientImpl(networkSettings.getConsensusNodes().iterator().next());
    }

    @NonNull
    public Account getOperatorAccount() {
        return operatorAccount;
    }

    @NonNull
    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }
}
