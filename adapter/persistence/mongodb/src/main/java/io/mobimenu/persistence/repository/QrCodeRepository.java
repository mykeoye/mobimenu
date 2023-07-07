package io.mobimenu.persistence.repository;

import io.mobimenu.domain.QrCode;
import io.mobimenu.domain.filters.QrCodeFilter;
import io.mobimenu.persistence.common.MongoOperator;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.QrCodeEntity;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class QrCodeRepository implements ReactivePanacheMongoRepository<QrCodeEntity> {

    public Uni<List<QrCodeEntity>> save(List<QrCodeEntity> entities) {
        return mongoCollection().insertMany(entities).flatMap(insertManyResult -> {
            List<ObjectId> ids = insertManyResult.getInsertedIds().values()
                    .stream()
                    .map(BsonValue::asObjectId)
                    .map(BsonObjectId::getValue)
                    .toList();
            return findByIds(ids);
        });
    }

    public Uni<List<QrCodeEntity>> findByIds(List<ObjectId> ids) {
        return find("%s in :ids".formatted(Fields._ID), Parameters.with(Fields.IDS, ids)).list();
    }

    public Uni<List<QrCodeEntity>> findByNumbersAndRestaurant(List<String> tableNumbers, String restaurantId) {
        return find("%s in :tableNumber and %s = :restaurantId".formatted(Fields.TABLE_NUMBER, Fields.RESTAURANT_ID),
                Parameters.with(Fields.TABLE_NUMBER, tableNumbers).and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).list();
    }

    public Uni<QrCodeEntity> findByQrCodeId(String qrCodeId) {
        return findById(new ObjectId(qrCodeId));
    }

    public Uni<Long> countAllByRestaurant(String restaurantId) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).count();
    }

    public Uni<List<QrCodeEntity>> findAllByRestaurant(String restaurantId, int page, int limit) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).page(Page.of(page, limit)).list();
    }

    public Uni<QrCodeEntity> findByRestaurantTypeAndNumber(String restaurantId, QrCode.Type type, String tableNumber) {
        return find("{'%s': :restaurantId, '%s': :type, '%s': :tableNumber}".formatted(Fields.RESTAURANT_ID, Fields.TYPE, Fields.TABLE_NUMBER),
                    Parameters.with(Fields.RESTAURANT_ID, new ObjectId(restaurantId))
                        .and(Fields.TYPE, type)
                        .and(Fields.TABLE_NUMBER, tableNumber))
                        .firstResult();
    }

    public Uni<QrCodeEntity> findByRestaurantAndCodeId(String restaurantId, String qrCodeId) {
        return find("%s = :restaurantId and %s = :_id".formatted(Fields.RESTAURANT_ID, Fields._ID),
                Parameters.with(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).and(Fields._ID, new ObjectId(qrCodeId))).firstResult();
    }

    public Uni<Long> countByFilter(QrCodeFilter filter) {
        return find(searchFilterToDocumentFilter(filter)).count();
    }

    public Uni<List<QrCodeEntity>> findByFilter(QrCodeFilter filter, int page, int limit) {
        return find(searchFilterToDocumentFilter(filter)).page(Page.of(page, limit)).list();
    }

    private Document searchFilterToDocumentFilter(QrCodeFilter filter) {
        var query = new Document();
        if (filter.getRestaurantId() != null) {
            query.put(Fields.RESTAURANT_ID, new ObjectId(filter.getRestaurantId()));
        }
        if (filter.getStatus() != null) {
            query.put(Fields.STATUS, filter.getStatus());
        } else {
            // So if the api doesn't have a status filter only return the live codes (Live codes are codes that are either
            // active or inactive)
            query.put(Fields.STATUS, new Document(MongoOperator.IN, List.of(QrCode.Status.ACTIVE, QrCode.Status.INACTIVE)));
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
