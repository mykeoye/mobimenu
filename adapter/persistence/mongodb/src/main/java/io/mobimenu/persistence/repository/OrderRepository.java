package io.mobimenu.persistence.repository;

import java.math.BigDecimal;
import java.util.List;
import io.mobimenu.domain.Order;
import io.mobimenu.persistence.projections.TotalSale;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.filters.OrderFilter;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.common.MongoOperator;
import org.bson.Document;
import org.bson.types.ObjectId;
import javax.enterprise.context.ApplicationScoped;
import io.mobimenu.persistence.entity.OrderEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;

@ApplicationScoped
public class OrderRepository implements ReactivePanacheMongoRepository<OrderEntity> {

    public Uni<OrderEntity> findByOrderId(String orderId) {
        return findById(new ObjectId(orderId));
    }

    public Uni<OrderEntity> findByRestaurantAndOrder(String restaurantId, String orderId) {
        return find("%s = :restaurantId and %s = :_id".formatted(Fields.RESTAURANT_ID, Fields._ID),
                Parameters.with(Fields.RESTAURANT_ID, new ObjectId(restaurantId))
                .and(Fields._ID, new ObjectId(orderId))).firstResult();
    }

    public Uni<Long> countAllByRestaurant(String restaurantId) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).count();
    }

    public Uni<BigDecimal> getTotalSales(OrderFilter filter) {
        return find(totalCostQuery(filter)).project(TotalSale.class).firstResult().map(TotalSale::totalSales);
    }

    public Uni<List<OrderEntity>> findByRestaurant(String restaurantId, int page, int limit) {
        return find("%s = :restaurantId".formatted(Fields.RESTAURANT_ID), Parameters.with(
                Fields.RESTAURANT_ID, new ObjectId(restaurantId)
        )).page(Page.of(page, limit)).list();
    }

    public Uni<Long> countAllByFilter(OrderFilter filter) {
        return find(searchFilterToDocumentFilter(filter)).count();
    }

    public Uni<List<OrderEntity>> findByFilter(OrderFilter filter, int page, int limit) {
        return find(searchFilterToDocumentFilter(filter)).page(Page.of(page, limit)).list();
    }

    private Document searchFilterToDocumentFilter(OrderFilter filter) {
        var query = new Document();
        if (filter.getRestaurantId() != null) {
            query.put(Fields.RESTAURANT_ID, new ObjectId(filter.getRestaurantId()));
        }
        if (filter.getStatus() != null) {
            query.put(Fields.STATUS, filter.getStatus());
        }
        if (filter.getPaymentStatus() != null) {
            query.put(Fields.PAYMENT_STATUS, filter.getPaymentStatus());
        }
        if (filter.getCustomerId() != null) {
            query.put(Fields.CUSTOMER_ID, new ObjectId(filter.getCustomerId()));
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

    private Document totalCostQuery(OrderFilter filter) {
        var query = new Document();
        if (filter.getRestaurantId() != null) {
            query.put(Fields.RESTAURANT_ID, new ObjectId(filter.getRestaurantId()));
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
        query.put(Fields.PAYMENT_STATUS, Order.PaymentStatus.PAID.name());
        query.put(MongoOperator.PROJECTION, new Document(Fields.TOTAL_SALES, new Document(MongoOperator.SUM, Fields.$ORDER_TOTAL_COST)));
        return query;
    }

}
