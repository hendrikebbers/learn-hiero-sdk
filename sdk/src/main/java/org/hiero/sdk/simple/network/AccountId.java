package org.hiero.sdk.simple.network;

import java.util.regex.Pattern;
import org.hiero.sdk.simple.network.keys.PublicKey;

public record AccountId(long shard,
                        long realm,
                        long num,
                        String checksum,
                        PublicKey aliasKey,
                        EvmAddress evmAddress) {

    private static final Pattern ENTITY_ID_REGEX =
            Pattern.compile("(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-([a-z]{5}))?$");

    public static AccountId fromString(String id) {
        if ((id.startsWith("0x") && id.length() == 42) || id.length() == 40) {
            throw new IllegalArgumentException("Creation based on EVM address is not supported.");
        }
        var match = ENTITY_ID_REGEX.matcher(id);
        if (match.find()) {
            return new AccountId(
                    Long.parseLong(match.group(1)),
                    Long.parseLong(match.group(2)),
                    Long.parseLong(match.group(3)),
                    match.group(4),
                    null,
                    null);
        }
        throw new IllegalArgumentException("Invalid Account ID '" + id + "'");
    }
}
