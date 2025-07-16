package org.hiero.sdk.simple;

import com.hedera.hashgraph.sdk.TransactionId;
import org.hiero.sdk.simple.network.Account;
import org.jspecify.annotations.NonNull;

public interface HieroClient {

    @NonNull
    Account getOperatorAccount();

    @NonNull
    TransactionId generateTransactionId();

}
