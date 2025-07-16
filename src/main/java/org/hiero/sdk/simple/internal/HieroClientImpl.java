package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.TransactionId;
import org.hiero.sdk.simple.GrpcClient;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.internal.grpc.GrpcClientImpl;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.NetworkSettings;
import org.jspecify.annotations.NonNull;

public class HieroClientImpl implements HieroClient {

    private final Account operatorAccount;

    private final NetworkSettings networkSettings;

    public HieroClientImpl(@NonNull Account operatorAccount, @NonNull NetworkSettings networkSettings) {
        this.operatorAccount = operatorAccount;
        this.networkSettings = networkSettings;
    }

    @Override
    public @NonNull TransactionId generateTransactionId() {
        return TransactionId.generate(operatorAccount.accountId());
    }

    @Override
    public @NonNull GrpcClient getGrpcClient() {
        return new GrpcClientImpl(networkSettings.getConsensusNodes().iterator().next());
    }

}
