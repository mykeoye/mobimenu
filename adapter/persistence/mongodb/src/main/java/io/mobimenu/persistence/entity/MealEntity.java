package io.mobimenu.persistence.entity;

import java.util.Set;
import lombok.ToString;
import java.math.BigDecimal;
import io.mobimenu.domain.Meal;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@ToString
@MongoEntity(collection = "meals")
public class MealEntity extends BaseEntity {
    public String name;
    public Set<ObjectId> menus;
    public String description;
    public Set<String> imageURIs;
    public String videoURI;
    public BigDecimal normalPrice;
    public Meal.Status status;
    public ObjectId categoryId;
    public BigDecimal discountPrice;
    public long cookingDuration;
    public ObjectId restaurantId;
}
