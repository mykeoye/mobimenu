package io.mobimenu.core.port.out.order;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.filters.OrderFilter;
import java.math.BigDecimal;
import java.util.List;

public interface OrderQueryOperationsPort {

    Uni<Tuple2<Long, List<Order>>> getByRestaurant(String restaurantId, Page page);

    Uni<Tuple2<Long, List<Order>>> getByFilter(OrderFilter filter, Page page);

    Uni<Long> getCount();

    Uni<Order> getById(String orderId);

    Uni<Long> getCountByFilter(OrderFilter filter);

    Uni<Order> getByOrderIdAndRestaurantId(String orderId, String restaurantId);

    Uni<BigDecimal> getTotalSales(OrderFilter filter);

}
