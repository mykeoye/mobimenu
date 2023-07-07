package io.mobimenu.persistence.repository;

import io.mobimenu.domain.filters.UserFilter;
import io.mobimenu.persistence.common.MongoOperator;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.UserEntity;
import org.bson.Document;
import org.bson.types.ObjectId;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class UserRepository implements ReactivePanacheMongoRepository<UserEntity> {

    public Uni<UserEntity> findByUserId(String userId) {
        return findById(new ObjectId(userId));
    }

    public Uni<UserEntity> findByEmail(String email) {
        return find(Fields.EMAIL, email).firstResult();
    }

    public Uni<UserEntity> findByIdAndAccountType(String userId, AccountType accountType) {
        return find("%s = :userId and %s = :accountType".formatted(Fields.USER_ID, Fields.ACCOUNT_TYPE),
                Parameters.with(Fields.USER_ID, new ObjectId(userId)).and(Fields.ACCOUNT_TYPE, accountType))
                .firstResult();
    }

    public Uni<UserEntity> findByEmailAndAccountType(String email, AccountType accountType) {
        return find("%s = :email and %s = :accountType".formatted(Fields.EMAIL, Fields.ACCOUNT_TYPE),
                Parameters.with(Fields.EMAIL, email).and(Fields.ACCOUNT_TYPE, accountType))
                .firstResult();
    }

    public Uni<List<UserEntity>> findByAccountType(AccountType accountType) {
        return find("%s = :accountType".formatted(Fields.ACCOUNT_TYPE),
                Parameters.with(Fields.ACCOUNT_TYPE, accountType))
                .list();
    }

    public Uni<Long> countAllByRestaurant(String restaurantId) {
        return find(Fields.RESTAURANTS, Set.of(new ObjectId(restaurantId))).count();
    }

    public Uni<List<UserEntity>> findByAccountType(AccountType accountType, int offset, int limit) {
        return find("%s = :accountType".formatted(Fields.ACCOUNT_TYPE),
                Parameters.with(Fields.ACCOUNT_TYPE, accountType)).page(Page.of(offset, limit)).list();
    }

    public Uni<List<UserEntity>> findByFilter(UserFilter filter, int offset, int limit) {
        return find(searchFilterToQueryDocument(filter)).page(Page.of(offset, limit)).list();
    }

    public Uni<UserEntity> findByDeleted(Boolean deleted) {
        return find(Fields.DELETED, deleted).firstResult();
    }

    public Uni<Long> countByFilter(UserFilter filter) {
        return find(searchFilterToQueryDocument(filter)).count();
    }

    private Document searchFilterToQueryDocument(UserFilter filter) {
        var query = new Document();
        if (filter.getUserId() != null) {
            query.put(Fields._ID, new ObjectId(filter.getUserId()));
        }
        if (filter.getAccountType() != null) {
            query.put(Fields.ACCOUNT_TYPE, filter.getAccountType());
        }
        if (filter.getRestaurantId() != null) {
            query.put(Fields.RESTAURANTS, Set.of(new ObjectId(filter.getRestaurantId())));
        }
        var from = filter.getFrom();
        var to = filter.getTo();
        if (from != null && to != null) {
            query.put(MongoOperator.AND, List.of(
                    new Document(Fields.CREATED, new Document(MongoOperator.GTE, filter.getFrom())),
                    new Document(Fields.CREATED, new Document(MongoOperator.LTE, filter.getTo()))
            ));
        }
        if (from != null && to == null) {
            query.put(Fields.CREATED, new Document(MongoOperator.GTE, filter.getFrom()));
        }
        if (from == null && to != null) {
            query.put(Fields.CREATED, new Document(MongoOperator.LTE, filter.getTo()));
        }
        if (filter.getDeleted() != null) {
            query.put(Fields.DELETED, filter.getDeleted());
        }
        return query;
    }

}