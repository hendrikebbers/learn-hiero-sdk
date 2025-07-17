package org.hiero.sdk.simple.transactions;

import org.hiero.sdk.simple.TransactionResponse;
import org.hiero.sdk.simple.network.AccountId;

public record AccountCreateResponse(AccountId accountId) implements TransactionResponse {
}
