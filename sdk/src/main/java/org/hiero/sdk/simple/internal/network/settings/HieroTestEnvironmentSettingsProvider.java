package org.hiero.sdk.simple.internal.network.settings;

import com.google.auto.service.AutoService;
import java.util.Optional;
import java.util.Set;
import org.hiero.sdk.simple.network.ConsensusNode;
import org.hiero.sdk.simple.network.settings.NetworkSettings;
import org.hiero.sdk.simple.network.settings.spi.NetworkSettingsProvider;
import org.jspecify.annotations.NonNull;

/**
 * Network settings for the Hedera Testnet.
 */
@AutoService(NetworkSettingsProvider.class)
public final class HieroTestEnvironmentSettingsProvider implements NetworkSettingsProvider {

    /**
     * The network identifier.
     */
    public static final String NETWORK_IDENTIFIER = "hiero-test";


    @Override
    public @NonNull String getName() {
        return NETWORK_IDENTIFIER;
    }

    @Override
    public @NonNull Set<NetworkSettings> createNetworkSettings() {
        return Set.of(new NetworkSettings() {
            @Override
            public @NonNull String getNetworkIdentifier() {
                return NETWORK_IDENTIFIER;
            }

            @Override
            public @NonNull Optional<String> getNetworkName() {
                return Optional.of("Hiero test environment");
            }

            @Override
            public @NonNull Set<ConsensusNode> getConsensusNodes() {
                return Set.of(new ConsensusNode("127.0.0.1", "50211", "0.0.3"));
            }
        });
    }
}
