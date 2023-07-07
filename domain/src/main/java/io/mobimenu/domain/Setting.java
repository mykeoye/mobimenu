package io.mobimenu.domain;

import io.mobimenu.domain.enums.SettingType;
import java.util.Map;

/**
 * A setting on the system, used for storing preference information about a restaurant
 *
 * @param settingId     unique identifier for the setting object
 * @param type          the type of setting stored
 * @param description   this generally will appear on the ui to provide detail information about the setting
 * @param preferences   an object containing the preference information to store
 * @param restaurantId  the restaurant the setting is linked to
 */
public record Setting(String settingId, SettingType type, String description, Map<String, Preference> preferences, String restaurantId) {

    /**
     * A representation of the settings value to be persisted
     *
     * @param name          the name of the preference
     * @param description   a description of the preference
     * @param enabled       this flag determines if the preference is enabled or not
     */
    public record Preference(String name, String description, Boolean enabled) {

    }

}
