package org.hiero.sdk.simple.internal.grpc;

import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import java.util.Set;

public interface MethodDescriptorFactory<Request extends Transaction, Response extends TransactionResponse> {

    Set<Class<Request>> getSupportedRequestTypes();

    MethodDescriptor<Transaction, TransactionResponse> createMethodDescriptor(Request request);

    static <T extends Transaction> MethodDescriptorFactory forRequestType(Class<T> cls) {
        return null;
    }
}
