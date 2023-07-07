package io.mobimenu.persistence.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.ToString;
import org.bson.types.ObjectId;

@ToString
@MongoEntity(collection = "bank-accounts")
public class BankAccountEntity extends BaseEntity {
    public String accountName;
    public String accountNumber;
    public String bank;
    public ObjectId restaurantId;
}
