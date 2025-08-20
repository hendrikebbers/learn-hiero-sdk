package org.hiero.sdk.simple.transactions;

import org.hiero.sdk.simple.Record;
import org.hiero.sdk.simple.network.AccountId;

public record AccountCreateRecord(AccountId createdAccount,
                                  AccountCreateReceipt receipt) implements
        Record<AccountCreateReceipt> {
}
