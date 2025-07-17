package org.hiero.sdk.simple;

import java.time.Duration;
import org.hiero.sdk.simple.network.Hbar;
import org.jspecify.annotations.NonNull;

public interface Transaction<T extends Transaction, R extends TransactionResponse> {

    Hbar getFee();

    void setFee(Hbar fee);

    T withFee(Hbar fee);

    Duration getValidDuration();

    void setValidDuration(Duration validDuration);

    T withValidDuration(Duration validDuration);

    String getMemo();

    void setMemo(String memo);

    T withMemo(String memo);

    FrozenTransaction<R> freezeTransaction(@NonNull HieroClient client);
}
