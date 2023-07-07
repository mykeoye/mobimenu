package io.mobimenu.persistence.entity;

import io.mobimenu.domain.enums.SettingType;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.ToString;
import java.util.HashMap;
import java.util.Map;

@ToString
@MongoEntity(collection = "settings")
public class SettingEntity extends BaseEntity {

    public SettingType type;
    public String restaurantId;
    public String description;
    public Map<String, Preference> preferences = new HashMap<>();

    public static class Preference {
        public String name;
        public Boolean enabled = false;
        public String description;
    }
}
