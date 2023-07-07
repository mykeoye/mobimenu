package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.filters.OrderFilter;
import io.mobimenu.persistence.mapper.OrderMapper;
import io.mobimenu.persistence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;

@RequiredArgsConstructor
public class OrderQueryAdapter implements OrderQueryOperationsPort {

    private final OrderRepository repository;
    private final OrderMapper mapper = OrderMapper.INSTANCE;

    @Override
    public Uni<Tuple2<Long, List<Order>>> getByRestaurant(String restaurantId, Page page) {
        var count = repository.countAllByRestaurant(restaurantId);
        var ordersUni = repository.findByRestaurant(restaurantId, page.getOffset(), page.getLimit())
                .map(orders -> Optional.ofNullable(orders).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
        return Uni.combine().all().unis(count, ordersUni).asTuple();
    }

    @Override
    public Uni<Tuple2<Long, List<Order>>> getByFilter(OrderFilter filter, Page page) {
        var count = repository.countAllByFilter(filter);
        var ordersUni = repository.findByFilter(filter, page.getOffset(), page.getLimit())
                .map(orders -> Optional.ofNullable(orders).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
        return Uni.combine().all().unis(count, ordersUni).asTuple();
    }

    @Override
    public Uni<Long> getCount() {
        return repository.countAllByFilter(OrderFilter.from(null, null, null));
    }

    @Override
    public Uni<Order> getById(String orderId) {
        return repository.findByOrderId(orderId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Long> getCountByFilter(OrderFilter filter) {
        return repository.countAllByFilter(filter);
    }

    @Override
    public Uni<Order> getByOrderIdAndRestaurantId(String orderId, String restaurantId) {
        return repository.findByRestaurantAndOrder(restaurantId, orderId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<BigDecimal> getTotalSales(OrderFilter filter) {
        return Uni.createFrom().item(() -> BigDecimal.ZERO);
    }

}
