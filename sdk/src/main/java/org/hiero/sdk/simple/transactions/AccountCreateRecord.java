package org.hiero.sdk.simple.transactions;

import org.hiero.sdk.simple.Record;
import org.hiero.sdk.simple.network.AccountId;
import org.hiero.sdk.simple.network.TransactionId;

public record AccountCreateRecord(TransactionId transactionId, AccountId createdAccount) implements Record {
}
