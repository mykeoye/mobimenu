package io.mobimenu.persistence;

import java.time.Instant;
import java.util.List;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import io.mobimenu.domain.Order;
import io.mobimenu.persistence.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import io.mobimenu.persistence.repository.OrderRepository;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;

@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderSaveOperationsPort {

    private final OrderRepository repository;
    private final OrderMapper mapper = OrderMapper.INSTANCE;

    @Override
    public Uni<Order> saveOrder(Order order) {
        return repository.persist(mapper.domainObjectToPersistEntity(order)).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Void> saveOrders(List<Order> orders) {
        return repository.persist(mapper.domainObjectsToPersistentEntity(orders));
    }

    @Override
    public Uni<Order.Status> updateStatus(String orderId, Order.Status status) {
        return repository.findById(new ObjectId(orderId))
                .map(order -> {
                    order.status = status;
                    order.updated = Instant.now();
                    return order;
                })
                .flatMap(repository::update)
                .map(o -> o.status);
    }

    @Override
    public Uni<Order.PaymentStatus> updatePaymentStatus(String orderId, Order.PaymentStatus paymentStatus) {
        return repository.findById(new ObjectId(orderId))
                .map(order -> {
                    order.paymentStatus = paymentStatus;
                    order.updated = Instant.now();
                    return order;
                })
                .flatMap(repository::update)
                .map(updated -> updated.paymentStatus);
    }

    @Override
    public Uni<Order> updateOrder(Order order) {
        return repository.findById(new ObjectId(order.getOrderId()))
                .map(entity -> {
                    entity.waitTimeInMinutes = order.getWaitTimeInMinutes();
                    entity.status = order.getStatus();
                    entity.numOfItems = order.getNumOfItems();
                    entity.paymentStatus = order.getPaymentStatus();
                    entity.totalCost = order.getTotalCost();
                    entity.salesChannel = order.getSalesChannel();
                    entity.updated = Instant.now();
                    return entity;
                })
                .flatMap(repository::update)
                .map(mapper::entityToDomainObject);
    }

}
