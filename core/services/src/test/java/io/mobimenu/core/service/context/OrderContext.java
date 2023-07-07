package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.order.PlaceOrderUseCase;
import io.mobimenu.core.port.out.events.DomainEventPublisher;
import io.mobimenu.core.port.out.meal.MealQueryOperationsPort;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;
import io.mobimenu.core.port.out.payment.PaymentSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.order.OrderService;
import io.mobimenu.domain.Order;
import io.mobimenu.persistence.OrderPersistenceAdapter;
import io.mobimenu.persistence.OrderQueryAdapter;
import io.mobimenu.persistence.repository.OrderRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

@ApplicationScoped
public class OrderContext {

    @Produces
    @Singleton
    @Named("orderDomainEventPublisher")
    public DomainEventPublisher<Order> domainEventPublisher() {
        return event -> System.out.println("Publishing event: " + event);
    }

    @Produces
    @Singleton
    public OrderQueryOperationsPort orderQueryOperationsPort(OrderRepository repository) {
        return new OrderQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public OrderSaveOperationsPort orderSaveOperationsPort(OrderRepository repository) {
        return new OrderPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public PlaceOrderUseCase placeOrderUseCase(OrderQueryOperationsPort orderQueryOperationsPort,
                                               RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                               OrderSaveOperationsPort orderSaveOperationsPort,
                                               MealQueryOperationsPort mealQueryOperationsPort,
                                               PaymentSaveOperationsPort paymentSaveOperationsPort,
                                               DomainEventPublisher<Order> domainEventPublisher) {
        return new OrderService(restaurantQueryOperationsPort, mealQueryOperationsPort, orderSaveOperationsPort, orderQueryOperationsPort, paymentSaveOperationsPort, domainEventPublisher);
    }

}
