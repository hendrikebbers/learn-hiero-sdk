package org.hiero.sdk.simple.internal.grpc;

import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientCall.Listener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import java.util.concurrent.CompletableFuture;
import org.hiero.sdk.simple.grpc.GrpcClient;
import org.hiero.sdk.simple.network.ConsensusNode;

public class GrpcClientImpl implements GrpcClient {

    private final Channel channel;

    public GrpcClientImpl(ConsensusNode node) {
        this(GrpcChannelFactory.createChannel(node));
    }

    public GrpcClientImpl(Channel channel) {
        this.channel = channel;
    }

    public CompletableFuture<TransactionResponse> sendTransaction(Transaction transaction,
            MethodDescriptor<Transaction, TransactionResponse> methodDescriptor) {
        final CompletableFuture<TransactionResponse> future = new CompletableFuture<>();
        final ClientCall<Transaction, TransactionResponse> call = channel.newCall(methodDescriptor,
                CallOptions.DEFAULT);
        call.start(new Listener<>() {

            @Override
            public void onMessage(TransactionResponse response) {
                future.complete(response);
            }

            @Override
            public void onClose(Status status, Metadata trailers) {
                if (!future.isDone()) {
                    future.completeExceptionally(
                            new RuntimeException("Call failed with status: " + status, status.asException()));
                }
            }
        }, new Metadata());
        call.sendMessage(transaction);
        call.halfClose();
        call.request(1);
        return future;
    }
}
