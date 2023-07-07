package io.mobimenu.persistence.entity;

import io.mobimenu.domain.Order;
import io.mobimenu.domain.enums.SalesChannel;
import lombok.ToString;
import org.bson.types.ObjectId;
import java.math.BigDecimal;
import java.util.Set;
import io.quarkus.mongodb.panache.common.MongoEntity;

@ToString
@MongoEntity(collection = "orders")
public class OrderEntity extends BaseEntity {
    public long numOfItems;
    public long waitTimeInMinutes;
    public String tableNumber;
    public String orderNum;
    public ObjectId restaurantId;
    public ObjectId customerId;
    public String customerName;
    public BigDecimal totalCost;
    public SalesChannel salesChannel;
    public Order.Status status;
    public Order.PaymentStatus paymentStatus;
    public Set<OrderItem> items;
    public BigDecimal vatAmount;
    public BigDecimal deliveryFee;

    public static class OrderItem {
        public String name;
        public BigDecimal price;
        public long quantity;
        public long cookingDurationInMin;
    }
}
