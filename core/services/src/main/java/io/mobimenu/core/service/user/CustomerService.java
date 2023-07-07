package io.mobimenu.core.service.user;

import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.user.ViewCustomerUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.domain.User;
import io.mobimenu.domain.enums.AccountType;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.in.user.CreateCustomerUseCase;

@Slf4j
@RequiredArgsConstructor
public class CustomerService implements CreateCustomerUseCase, ViewCustomerUseCase {

    private final UserSaveOperationsPort userSaveOperationsPort;
    private final UserQueryOperationsPort userQueryOperationsPort;
    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;

    @Override
    public Uni<User> createCustomer(CreateCustomerCommand command) {
        return restaurantQueryOperationsPort.getById(command.getRestaurantId())
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .flatMap(ignored -> userQueryOperationsPort.getByEmail(command.getEmail()))
                .flatMap(customer -> {
                    if (customer == null) {
                        return userSaveOperationsPort.saveUser(User.withCustomerFields(
                                command.getFirstName(),
                                command.getLastName(),
                                command.getEmail(),
                                command.getPhoneNumber(),
                                Set.of(command.getRestaurantId())
                        ));
                    }
                    // We don't want to create the same customer account multiple times on the system. But the same customer
                    // can be a customer in many restaurants
                    Set<String> restaurants = Optional.ofNullable(customer.getRestaurants()).orElseGet(Set::of);
                    if (restaurants.contains(command.getRestaurantId()) || !AccountType.CUSTOMER.equals(customer.getAccountType())) {
                        return Uni.createFrom().failure(Failure.of(Code.USER_EXISTS));
                    }
                    return userSaveOperationsPort.linkRestaurantToUser(customer.getEmail(), command.getRestaurantId(), AccountType.CUSTOMER)
                            .map(u -> u);
                });
    }

    @Override
    public Uni<User> loadCustomerById(String userId) {
        return userQueryOperationsPort.getByIdAndAccountType(userId, AccountType.CUSTOMER)
                .onItem().ifNull().failWith(Failure.of(Code.CUSTOMER_NOT_FOUND));
    }

    @Override
    public Uni<Tuple2<Long, List<User>>> loadCustomersByRestaurant(String restaurantId, Page page) {
        return userQueryOperationsPort.getAllByRestaurantIdAndAccountType(restaurantId, AccountType.CUSTOMER, page);
    }

}
