package io.mobimenu.domain.enums;

import java.util.Arrays;

public enum SettingType {

    PAYMENT, NOTIFICATION, SOCIAL;

    public static SettingType from(String type) {
        return Arrays.stream(SettingType.values())
                .filter(chan -> chan.name().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }
}
