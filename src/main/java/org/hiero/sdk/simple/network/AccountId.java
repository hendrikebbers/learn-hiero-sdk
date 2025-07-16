package org.hiero.sdk.simple.network;

import com.hedera.hashgraph.sdk.EvmAddress;
import com.hedera.hashgraph.sdk.PublicKey;

public record AccountId(long shard,
                        long realm,
                        long num,
                        String checksum,
                        PublicKey aliasKey,
                        EvmAddress evmAddress) {
}
