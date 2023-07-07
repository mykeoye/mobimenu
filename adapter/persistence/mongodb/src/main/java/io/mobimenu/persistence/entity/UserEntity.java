package io.mobimenu.persistence.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.mobimenu.domain.enums.AccountStatus;
import io.mobimenu.domain.enums.AccountType;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.Set;

@ToString
@MongoEntity(collection = "users")
public class UserEntity extends BaseEntity {
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String email;
    public boolean verified;
    public AccountType accountType;
    public String password;
    public AccountStatus accountStatus;
    public Set<ObjectId> restaurants;
    public Map<String, String> accountProperties;
}