package org.hiero.sdk.simple;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;
import org.hiero.sdk.simple.network.Account;
import org.jspecify.annotations.NonNull;

public interface Transaction<T extends Transaction, R extends TransactionResponse> {

    @NonNull
    default T freezeAndSignWithOperator(@NonNull final HieroClient client) {
        Objects.requireNonNull(client, "client must not be null");
        freeze(client);
        final Account account = client.getOperatorAccount();
        return sign(account.privateKey());
    }

    @NonNull
    T freeze(@NonNull HieroClient client);

    @NonNull
    default T sign(@NonNull final PrivateKey privateKey) {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        return sign(privateKey.getPublicKey(), privateKey::sign);
    }

    @NonNull
    T sign(@NonNull PublicKey publicKey, @NonNull UnaryOperator<byte[]> transactionSigner);

    @NonNull
    CompletableFuture<R> execute(@NonNull HieroClient client);

    default R executeAndWait(@NonNull HieroClient client) throws ExecutionException, InterruptedException {
        return execute(client).get();
    }

    default CompletableFuture<R> freezeSignAndExecute(@NonNull HieroClient client) {
        Objects.requireNonNull(client, "client must not be null");
        freezeAndSignWithOperator(client);
        return execute(client);
    }

    default R freezeSignExecuteAndWait(@NonNull HieroClient client)
            throws ExecutionException, InterruptedException {
        return freezeSignAndExecute(client).get();
    }

    Hbar getFee();

    void setFee(Hbar fee);

    T withFee(Hbar fee);

    Duration getValidDuration();

    void setValidDuration(Duration validDuration);

    T withValidDuration(Duration validDuration);

    String getMemo();

    void setMemo(String memo);

    T withMemo(String memo);
}
