package io.mobimenu.core.service.order;

import java.util.List;
import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;
import io.mobimenu.domain.Order;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.filters.OrderFilter;
import lombok.RequiredArgsConstructor;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.core.port.in.order.ViewOrderUseCase;

@RequiredArgsConstructor
public class OrderQueryService implements ViewOrderUseCase {

    private final OrderQueryOperationsPort orderQueryOperationsPort;

    @Override
    public Uni<Tuple2<Long, List<Order>>> loadOrdersByRestaurant(String restaurantId, Page page) {
        return orderQueryOperationsPort.getByRestaurant(restaurantId, page);
    }

    @Override
    public Uni<Tuple2<Long, List<Order>>> loadOrdersByFilter(OrderFilter filter, Page page) {
        return orderQueryOperationsPort.getByFilter(filter, page);
    }

    @Override
    public Uni<Order> loadOrderById(String orderId) {
        return orderQueryOperationsPort.getById(orderId);
    }

}
