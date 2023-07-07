package io.mobimenu.persistence.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.mobimenu.domain.Tag;
import lombok.ToString;
import org.bson.types.ObjectId;

@ToString
@MongoEntity(collection = "tags")
public class TagEntity extends BaseEntity {
    public String name;
    public String icon;
    public Tag.Status status;
    public ObjectId restaurantId;
}
