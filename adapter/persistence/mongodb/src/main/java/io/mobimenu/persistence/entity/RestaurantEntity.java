package io.mobimenu.persistence.entity;

import java.util.Set;
import lombok.ToString;
import io.mobimenu.domain.WorkPeriod;
import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.enums.SalesChannel;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@ToString
@MongoEntity(collection = "restaurants")
public class RestaurantEntity extends BaseEntity {
    public String name;
    public String logoUri;
    public String email;
    public String type;
    public String address;
    public ObjectId ownerId;
    public Set<SalesChannel> salesChannels;
    public PhoneNumber phoneNumber;
    public WorkPeriod workPeriod;
    public ThemeEntity theme;
}

