package org.hiero.sdk.simple.transactions;

import com.hedera.hashgraph.sdk.proto.AccountID;
import com.hedera.hashgraph.sdk.proto.TransactionReceipt;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.internal.AbstractResponse;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.network.AccountId;
import org.hiero.sdk.simple.network.TransactionId;
import org.jspecify.annotations.NonNull;

public final class AccountCreateResponse extends AbstractResponse<AccountCreateReceipt> {

    public AccountCreateResponse(@NonNull HieroClient hieroClient, @NonNull final TransactionId transactionId) {
        super(hieroClient, transactionId, (id, r) -> createReceipt(id, r));
    }

    private static AccountCreateReceipt createReceipt(TransactionId transactionId, TransactionReceipt receipt) {
        final AccountID accountIdProto = receipt.getAccountID();
        if (accountIdProto == null) {
            throw new IllegalStateException("Account ID is null in the receipt");
        }
        AccountId accountID = ProtobufUtil.fromProtobuf(accountIdProto);
        return new AccountCreateReceipt(transactionId, accountID);
    }
}
