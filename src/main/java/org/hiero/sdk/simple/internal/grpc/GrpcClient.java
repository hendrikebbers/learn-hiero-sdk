package org.hiero.sdk.simple.internal.grpc;

import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.Grpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.hiero.sdk.simple.network.ConsensusNode;

public class GrpcClient {

    private final static Executor executor = Executors.newCachedThreadPool();

    private final Channel channel;

    public GrpcClient(Channel channel) {
        this.channel = channel;
    }

    public <Request extends com.hedera.hashgraph.sdk.proto.Transaction> CompletableFuture<TransactionResponse> sendTransaction(
            Request request) {
        final CompletableFuture<TransactionResponse> future = new CompletableFuture<>();
        final ClientCall<Request, TransactionResponse> call = createCall(request);
        call.start(new io.grpc.ClientCall.Listener<TransactionResponse>() {

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
        }, new io.grpc.Metadata());
        call.sendMessage(request);
        call.halfClose();
        call.request(1);
        return future;
    }

    private <Request extends com.hedera.hashgraph.sdk.proto.Transaction> ClientCall<Request, TransactionResponse> createCall(
            Request request) {
        final MethodDescriptorFactory methodDescriptorFactory = MethodDescriptorFactory.forRequestType(
                request.getClass());
        final MethodDescriptor<Request, TransactionResponse> methodDescriptor = methodDescriptorFactory.createMethodDescriptor(request);
        final CallOptions callOptions = CallOptions.DEFAULT;
        return channel.newCall(methodDescriptor, callOptions);
    }

    public static Channel createChannel(ConsensusNode node) {
            final ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forTarget(node.getAddress()).usePlaintext();
            return channelBuilder.keepAliveTimeout(10L, TimeUnit.SECONDS)
                    .keepAliveWithoutCalls(true)
                    .disableRetry()
                    .executor(executor)
                    .build();
    }
}
