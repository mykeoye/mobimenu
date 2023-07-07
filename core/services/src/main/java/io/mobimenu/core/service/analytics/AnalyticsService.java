package io.mobimenu.core.service.analytics;

import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.filters.UserFilter;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.filters.DateRange;
import io.mobimenu.core.port.in.analytics.ViewDashboardAnalyticsUseCase;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;
import io.mobimenu.domain.Datum;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.filters.OrderFilter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class AnalyticsService implements ViewDashboardAnalyticsUseCase {

    private final OrderQueryOperationsPort orderQueryOperationsPort;
    private final UserQueryOperationsPort userQueryOperationsPort;

    @Override
    public Uni<List<Datum>> loadOrderSummaryByRange(String restaurantId, DateRange dateRange) {

        var totalOrders = orderQueryOperationsPort.getCountByFilter(OrderFilter.from(
                restaurantId,
                null,
                null,
                null,
                dateRange
        ));

        var pendingOrders = orderQueryOperationsPort.getCountByFilter(OrderFilter.from(
                restaurantId,
                Order.Status.PENDING.name(),
                null,
                null,
                dateRange
        ));

        var acceptedOrders  = orderQueryOperationsPort.getCountByFilter(OrderFilter.from(
                restaurantId,
                Order.Status.CONFIRMED.name(),
                null,
                null,
                dateRange
        ));

        var declinedOrders = orderQueryOperationsPort.getCountByFilter(OrderFilter.from(
                restaurantId,
                Order.Status.DECLINED.name(),
                null,
                null,
                dateRange
        ));

        return Uni.join().all(totalOrders, pendingOrders, acceptedOrders, declinedOrders).andCollectFailures()
                .map(t -> List.of(
                        new Datum(Datum.Type.TOTAL_ORDERS, Long.toString(t.get(0))),
                        new Datum(Datum.Type.PENDING_ORDERS, Long.toString(t.get(1))),
                        new Datum(Datum.Type.ACCEPTED_ORDERS, Long.toString(t.get(2))),
                        new Datum(Datum.Type.DECLINED_ORDERS, Long.toString(t.get(3)))
                ));
    }

    @Override
    public Uni<List<Datum>> loadDashboardSummaryByRange(String restaurantId, DateRange dateRange) {
        var filter = OrderFilter.from(restaurantId, dateRange);
        var totalSales = orderQueryOperationsPort.getTotalSales(filter);
        var totalOrders = orderQueryOperationsPort.getCountByFilter(filter);
        var declinedOrders =  orderQueryOperationsPort.getCountByFilter(OrderFilter.from(
                restaurantId,
                Order.Status.DECLINED.name(),
                null,
                null,
                dateRange
        ));
        var totalCustomers = userQueryOperationsPort.getCountByFilter(UserFilter.from(null, restaurantId, AccountType.CUSTOMER, dateRange));
        return Uni.combine().all().unis(totalSales, totalOrders, declinedOrders, totalCustomers).asTuple()
                .map(t -> List.of(
                        new Datum(Datum.Type.TOTAL_SALES, t.getItem1().toPlainString()),
                        new Datum(Datum.Type.TOTAL_ORDERS, Long.toString(t.getItem2())),
                        new Datum(Datum.Type.DECLINED_ORDERS, Long.toString(t.getItem3())),
                        new Datum(Datum.Type.TOTAL_CUSTOMERS, Long.toString(t.getItem4()))
                ));
    }


}
