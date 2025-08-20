package org.hiero.sdk.simple.internal;

import org.hiero.sdk.simple.Record;

public record DefaultRecord(DefaultReceipt receipt) implements
        Record<DefaultReceipt> {

}
