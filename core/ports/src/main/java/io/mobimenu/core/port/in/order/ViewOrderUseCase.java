package io.mobimenu.core.port.in.order;

import java.util.List;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.domain.Order;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.filters.OrderFilter;

public interface ViewOrderUseCase {

    Uni<Tuple2<Long, List<Order>>> loadOrdersByRestaurant(String restaurantId, Page page);

    Uni<Tuple2<Long, List<Order>>> loadOrdersByFilter(OrderFilter filter, Page page);

    Uni<Order> loadOrderById(String orderId);

}
