package org.hiero.sdk.simple.grpc;

import com.google.protobuf.MessageLite;
import io.grpc.MethodDescriptor;
import java.util.concurrent.CompletableFuture;

public interface GrpcClient {

    <I extends MessageLite, O extends MessageLite> CompletableFuture<O> call(
            MethodDescriptor<I, O> methodDescriptor, I input);

}
