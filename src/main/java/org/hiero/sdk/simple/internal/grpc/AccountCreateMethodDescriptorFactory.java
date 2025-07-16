package org.hiero.sdk.simple.internal.grpc;

import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.Transaction;
import io.grpc.MethodDescriptor;
import java.util.Set;

public class AccountCreateMethodDescriptorFactory implements MethodDescriptorFactory {
    
    @Override
    public Set<Class> getSupportedRequestTypes() {
        return Set.of();
    }

    @Override
    public MethodDescriptor createMethodDescriptor(Transaction transaction) {
        return CryptoServiceGrpc.getCreateAccountMethod();
    }
}
