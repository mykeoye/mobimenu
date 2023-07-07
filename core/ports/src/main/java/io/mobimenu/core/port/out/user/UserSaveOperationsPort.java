package io.mobimenu.core.port.out.user;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.User;

import java.util.Map;

public interface UserSaveOperationsPort {

    Uni<User> saveUser(User user);

    Uni<User> linkRestaurantToUser(String email, String restaurantId, AccountType accountType);

    Uni<User> updateUser(User user);

    Uni<User> updateEmployee(String userId, String firstName, String lastName, PhoneNumber phoneNumber,
                             Map<String, String> accountProperties);

    Uni<User> updateUserProfile(String userId, String firstName, String lastName, String email);

    Uni<String> deleteUser(String userId);
}