package io.mobimenu.persistence;

import io.netty.util.internal.StringUtil;
import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.User;
import io.mobimenu.persistence.mapper.UserMapper;
import io.mobimenu.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * This class implements all save and update operations on the user entity
 */
@Slf4j
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserSaveOperationsPort {

    private final UserRepository userRepository;
    private final UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public Uni<User> saveUser(User user) {
        var entity = mapper.domainObjectToPersistEntity(user);
        return userRepository.persist(entity).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<User> linkRestaurantToUser(String email, String restaurantId, AccountType accountType) {
        return userRepository.findByEmailAndAccountType(email, accountType)
                .flatMap(user -> {
                    user.restaurants.add(new ObjectId(restaurantId));
                    return userRepository.update(user);
                })
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<User> updateUser(User user) {
        return userRepository.findByUserId(user.getUserId())
                .flatMap(userRepository::update)
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<User> updateEmployee(String userId, String firstName, String lastName, PhoneNumber phoneNumber,
                                    Map<String, String> accountProperties) {
        // TODO - Refactor this piece of code. Just enforce the required fields at the controller level
        return userRepository.findByUserId(userId)
                .map(employee -> {
                    if (!StringUtil.isNullOrEmpty(firstName)) {
                        employee.firstName = firstName;
                    }
                    if (!StringUtil.isNullOrEmpty(lastName)) {
                        employee.lastName = lastName;
                    }

                    if (phoneNumber != null) {
                        employee.phoneNumber = phoneNumber.toString();
                    }
                    if (!accountProperties.isEmpty()){
                        employee.accountProperties = accountProperties;
                    }
                    return employee;
                }).flatMap(userRepository::update)
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<String> deleteUser(String userId) {
        return userRepository.findById(new ObjectId(userId))
                .map(entity -> {
                    entity.deleted = true;
                    return entity;
                })
                .flatMap(userRepository::update)
                .map(entity -> entity.id.toString());
    }

    @Override
    public Uni<User> updateUserProfile(String userId, String firstName, String lastName, String email) {
        return userRepository.findById(new ObjectId(userId)).map(
                user -> {
                    user.firstName = firstName;
                    user.lastName = lastName;
                    user.email = email;
                    return user;
                })
                .flatMap(userRepository::update)
                .map(mapper::entityToDomainObject);
    }

}