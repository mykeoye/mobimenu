package io.mobimenu.common.util;

import java.util.Objects;
import java.util.UUID;

public final class IdentifierProvider {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String uuid(String prefix) {
        return "%s-%s".formatted(Objects.requireNonNull(prefix).toLowerCase(), UUID.randomUUID().toString());
    }

}
