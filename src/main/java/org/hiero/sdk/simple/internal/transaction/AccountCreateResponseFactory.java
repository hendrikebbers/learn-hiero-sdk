package org.hiero.sdk.simple.internal.transaction;

import com.google.auto.service.AutoService;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import org.hiero.sdk.simple.transactions.AccountCreateResponse;
import org.hiero.sdk.simple.transactions.ResponseFactory;

@AutoService(ResponseFactory.class)
public class AccountCreateResponseFactory implements ResponseFactory<AccountCreateResponse> {

    @Override
    public Class<AccountCreateResponse> getSupportedResponseType() {
        return AccountCreateResponse.class;
    }

    @Override
    public AccountCreateResponse createResponse(TransactionResponse grpcResponse) {
        return new AccountCreateResponse(null);
    }
}
