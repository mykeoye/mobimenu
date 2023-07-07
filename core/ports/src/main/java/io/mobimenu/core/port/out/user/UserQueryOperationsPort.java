package io.mobimenu.core.port.out.user;

import io.mobimenu.domain.filters.UserFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.User;
import java.util.List;

public interface UserQueryOperationsPort {

    Uni<User> getByIdAndAccountType(String userId, AccountType accountType);

    Uni<User> getById(String userId);

    Uni<User> getByEmail(String emailAddress);

    Uni<User> getByEmailAndAccountType(String emailAddress, AccountType accountType);

    Uni<List<User>> getByAccountType(AccountType accountType);

    Uni<Long> getCountByFilter(UserFilter filter);

    Uni<Tuple2<Long, List<User>>> getAllByRestaurantIdAndAccountType(String restaurantId, AccountType accountType, Page page);

    Uni<User> getByDeleted(Boolean deleted);
}

