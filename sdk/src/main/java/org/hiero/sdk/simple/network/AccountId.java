package org.hiero.sdk.simple.network;

import java.util.Objects;
import java.util.regex.Pattern;
import org.jspecify.annotations.NonNull;

public record AccountId(long shard,
                        long realm,
                        long num,
                        String checksum) {

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
                    match.group(4));
        }
        throw new IllegalArgumentException("Invalid Account ID '" + id + "'");
    }

    @NonNull
    public static AccountId from(@NonNull final String part) {
        Objects.requireNonNull(part, "part must not be null");
        if (part == null || part.isEmpty()) {
            throw new IllegalArgumentException("Account ID part must not be null or empty");
        }
        final String[] parts = part.split("\\.");
        if (parts.length != 3 && parts.length != 4) {
            throw new IllegalArgumentException("Invalid Account ID part '" + part + "'");
        }
        final long shard = Long.parseLong(parts[0]);
        final long realm = Long.parseLong(parts[1]);
        final long num = Long.parseLong(parts[2]);
        final String checksum = parts.length == 4 ? parts[3] : null;
        return new AccountId(shard, realm, num, checksum);
    }

    @Override
    public String toString() {
        return shard + "." + realm + "." + num;
    }
}
