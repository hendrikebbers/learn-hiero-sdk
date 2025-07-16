package org.hiero.sdk.simple.transactions;

import com.hedera.hashgraph.sdk.AccountId;
import org.hiero.sdk.simple.TransactionResponse;

public record AccountCreateResponse(AccountId accountId) implements TransactionResponse {
}
