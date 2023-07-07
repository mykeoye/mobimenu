package io.mobimenu.core.port.out.order;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Order;
import java.util.List;

public interface OrderSaveOperationsPort {

    Uni<Order> saveOrder(Order order);

    Uni<Void> saveOrders(List<Order> orders);

    Uni<Order.Status> updateStatus(String orderId, Order.Status status);

    Uni<Order.PaymentStatus> updatePaymentStatus(String orderId, Order.PaymentStatus paymentStatus);

    Uni<Order> updateOrder(Order order);

}
