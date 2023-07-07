package io.mobimenu.persistence.entity;

import lombok.ToString;
import io.mobimenu.domain.Category;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@ToString
@MongoEntity(collection = "categories")
public class CategoryEntity extends BaseEntity {
    public String name;
    public ObjectId restaurantId;
    public boolean isDefault;
    public Category.Status status;
}
