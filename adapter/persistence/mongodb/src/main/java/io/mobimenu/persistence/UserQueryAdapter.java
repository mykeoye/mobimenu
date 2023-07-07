package io.mobimenu.persistence;

import io.mobimenu.domain.filters.UserFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.User;
import io.mobimenu.persistence.mapper.UserMapper;
import io.mobimenu.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
/**
 * Implements all user query related operations, strictly for retrievals
 */
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryOperationsPort {

    private final UserRepository repository;
    private final UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public Uni<User> getByIdAndAccountType(String userId, AccountType accountType) {
        return repository.findByIdAndAccountType(userId, accountType).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<User> getById(String userId) {
        return repository.findByUserId(userId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<User> getByEmail(String email) {
        return repository.findByEmail(email).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<User> getByEmailAndAccountType(String email, AccountType accountType) {
        return repository.findByEmailAndAccountType(email, accountType)
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<List<User>> getByAccountType(AccountType accountType) {
        return repository.findByAccountType(accountType)
                .map(users -> Optional.ofNullable(users).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
    }

    @Override
    public Uni<Long> getCountByFilter(UserFilter filter) {
        return repository.countByFilter(filter);
    }

    @Override
    public Uni<Tuple2<Long, List<User>>> getAllByRestaurantIdAndAccountType(String restaurantId, AccountType accountType, Page page) {
        var countUni = repository.countAllByRestaurant(restaurantId);
        var usersUni = repository.findByFilter(UserFilter.from(null, restaurantId, accountType), page.getOffset(), page.getLimit())
                .map(users -> Optional.ofNullable(users).orElseGet(List::of))
                .map(users -> users.stream().map(mapper::entityToDomainObject).toList());
        return Uni.combine().all().unis(countUni, usersUni).asTuple();
    }

    @Override
    public Uni<User> getByDeleted(Boolean deleted) {
        return repository.findByDeleted(deleted).map(mapper::entityToDomainObject);
    }
}