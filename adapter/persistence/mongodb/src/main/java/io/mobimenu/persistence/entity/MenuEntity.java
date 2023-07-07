package io.mobimenu.persistence.entity;

import io.mobimenu.domain.Menu;
import lombok.ToString;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@ToString
@MongoEntity(collection = "menus")
public class MenuEntity extends BaseEntity {
    public String name;
    public Menu.Status status;
    public ThemeEntity theme;
    public ObjectId restaurantId;
}
