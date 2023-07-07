package io.mobimenu.web.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mobimenu.core.port.in.order.PlaceOrderUseCase;
import io.mobimenu.core.port.in.order.ProcessOrderUseCase;
import io.mobimenu.core.port.in.order.UpdateOrderStatusUseCase;
import io.mobimenu.core.port.in.order.ViewOrderUseCase;
import io.mobimenu.core.port.out.events.DomainEventPublisher;
import io.mobimenu.core.port.out.meal.MealQueryOperationsPort;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.context.ApplicationScoped;
import io.mobimenu.core.port.out.payment.PaymentSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.order.OrderQueryService;
import io.mobimenu.core.service.order.OrderService;
import io.mobimenu.domain.Order;
import io.mobimenu.events.OrderDomainEventPublisher;
import io.mobimenu.persistence.OrderQueryAdapter;
import io.mobimenu.persistence.OrderPersistenceAdapter;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;
import io.mobimenu.persistence.repository.OrderRepository;
import io.vertx.mutiny.core.eventbus.EventBus;

@ApplicationScoped
public class OrderContext {

    @Produces
    @Singleton
    @Named("orderDomainEventPublisher")
    public DomainEventPublisher<Order> domainEventPublisher(ObjectMapper mapper, EventBus eventBus) {
        return new OrderDomainEventPublisher(mapper, eventBus);
    }

    @Produces
    @Singleton
    public OrderSaveOperationsPort orderSaveOperationsPort(OrderRepository repository) {
        return new OrderPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public OrderQueryOperationsPort orderQueryOperationsPort(OrderRepository repository) {
        return new OrderQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public PlaceOrderUseCase placeOrderUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                               MealQueryOperationsPort mealQueryOperationsPort,
                                               OrderQueryOperationsPort orderQueryOperationsPort,
                                               OrderSaveOperationsPort orderSaveOperationsPort,
                                               PaymentSaveOperationsPort paymentSaveOperationsPort,
                                               @Named("orderDomainEventPublisher") DomainEventPublisher<Order> domainEventPublisher) {
        return orderService(restaurantQueryOperationsPort, mealQueryOperationsPort, orderQueryOperationsPort, orderSaveOperationsPort, paymentSaveOperationsPort, domainEventPublisher);
    }

    @Produces
    @Singleton
    public ProcessOrderUseCase processOrderUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                   MealQueryOperationsPort mealQueryOperationsPort,
                                                   OrderQueryOperationsPort orderQueryOperationsPort,
                                                   OrderSaveOperationsPort orderSaveOperationsPort,
                                                   PaymentSaveOperationsPort paymentSaveOperationsPort) {
        return orderService(restaurantQueryOperationsPort, mealQueryOperationsPort, orderQueryOperationsPort, orderSaveOperationsPort, paymentSaveOperationsPort, null);
    }

    @Produces
    @Singleton
    public ViewOrderUseCase viewOrderUseCase(OrderQueryOperationsPort orderQueryOperationsPort) {
        return new OrderQueryService(orderQueryOperationsPort);
    }

    @Produces
    @Singleton
    public UpdateOrderStatusUseCase updateOrderStatusUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                             MealQueryOperationsPort mealQueryOperationsPort,
                                                             OrderQueryOperationsPort orderQueryOperationsPort,
                                                             OrderSaveOperationsPort orderSaveOperationsPort,
                                                             PaymentSaveOperationsPort paymentSaveOperationsPort) {
        return orderService(restaurantQueryOperationsPort, mealQueryOperationsPort, orderQueryOperationsPort, orderSaveOperationsPort, paymentSaveOperationsPort, null);

    }

    private OrderService orderService(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                      MealQueryOperationsPort mealQueryOperationsPort,
                                      OrderQueryOperationsPort orderQueryOperationsPort,
                                      OrderSaveOperationsPort orderSaveOperationsPort,
                                      PaymentSaveOperationsPort paymentSaveOperationsPort,
                                      @Named("orderDomainEventPublisher") DomainEventPublisher<Order> domainEventPublisher) {
        return new OrderService(restaurantQueryOperationsPort, mealQueryOperationsPort, orderSaveOperationsPort, orderQueryOperationsPort, paymentSaveOperationsPort, domainEventPublisher);
    }

}
