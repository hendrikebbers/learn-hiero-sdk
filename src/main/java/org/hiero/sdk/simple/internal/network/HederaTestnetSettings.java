package org.hiero.sdk.simple.internal.network;

import java.util.Optional;
import java.util.Set;
import org.hiero.sdk.simple.network.NetworkSettings;
import org.hiero.sdk.simple.network.ConsensusNode;
import org.jspecify.annotations.NonNull;

/**
 * Network settings for the Hedera Testnet.
 */
public final class HederaTestnetSettings implements NetworkSettings {

    /**
     * The network identifier.
     */
    public static final String NETWORK_IDENTIFIER = "hedera-testnet";

    @Override
    public @NonNull String getNetworkIdentifier() {
        return NETWORK_IDENTIFIER;
    }

    @Override
    public @NonNull Optional<String> getNetworkName() {
        return Optional.of("Hedera Testnet");
    }

    @Override
    public @NonNull Set<ConsensusNode> getConsensusNodes() {
        return Set.of();
    }

}
