package io.mobimenu.persistence.entity;

import io.mobimenu.domain.enums.ThemeType;
import lombok.ToString;
import io.quarkus.mongodb.panache.common.MongoEntity;

@ToString
@MongoEntity(collection = "themes")
public class ThemeEntity extends BaseEntity {
    public String name;
    public String primaryColor;
    public String accentColor;
    public String logo;
    public String backgroundImageURI;
    public ThemeType type;
}
