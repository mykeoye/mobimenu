package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.domain.filters.DateRange;
import io.mobimenu.core.port.in.analytics.ViewDashboardAnalyticsUseCase;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.Datum;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.enums.SalesChannel;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class ViewDashboardAnalyticsUseCaseTest extends AbstractUseCaseTest {

    @Inject
    OrderSaveOperationsPort orderSaveOperationsPort;

    @Inject
    ViewDashboardAnalyticsUseCase viewDashboardCanvasUseCase;

    @Test
    @DisplayName("A restaurant should be able to view the metrics of their orders")
    void restaurantShouldBeAbleToViewOrderSummary() {
        var restaurantId = ObjectId.get();
        var created = createOrders(restaurantId).subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();

        var date = LocalDate.now();
        var items = viewDashboardCanvasUseCase.loadOrderSummaryByRange(restaurantId.toString(), new DateRange(
                date.minusDays(1), date
        )).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().assertCompleted().getItem();

        assertNotNull(items);
        assertEquals(4, items.size());
        assertEquals(Datum.Type.TOTAL_ORDERS, items.get(0).type());
        assertEquals("4", items.get(0).value());

        assertEquals(Datum.Type.PENDING_ORDERS, items.get(1).type());
        assertEquals("1", items.get(1).value());

        assertEquals(Datum.Type.ACCEPTED_ORDERS, items.get(2).type());
        assertEquals("2", items.get(2).value());

        assertEquals(Datum.Type.DECLINED_ORDERS, items.get(3).type());
        assertEquals("1", items.get(3).value());

    }

    void restaurantShouldBeAbleToViewDashboardSummary() {
        var restaurantId = ObjectId.get();
        var created = createOrders(restaurantId).subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();

        var date = LocalDate.now();
        var items = viewDashboardCanvasUseCase.loadDashboardSummaryByRange(restaurantId.toString(), new DateRange(
                date.minusDays(1), date
        )).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().assertCompleted().getItem();

        assertNotNull(items);
        assertEquals(4, items.size());
        assertEquals(Datum.Type.TOTAL_SALES, items.get(0).type());
        assertEquals("4", items.get(0).value());

        assertEquals(Datum.Type.TOTAL_ORDERS, items.get(1).type());
        assertEquals("1", items.get(1).value());

        assertEquals(Datum.Type.DECLINED_ORDERS, items.get(2).type());
        assertEquals("2", items.get(2).value());

        assertEquals(Datum.Type.TOTAL_CUSTOMERS, items.get(3).type());
        assertEquals("1", items.get(3).value());

    }

    public Uni<Void> createOrders(ObjectId restaurantId) {
        var userId = ObjectId.get().toString();
        var order1 = Order.withRequiredFields(
                "1",
                SalesChannel.DELIVERY,
                Order.Status.CONFIRMED,
                "", Order.PaymentStatus.PENDING,
                restaurantId.toString(),
                userId,
                "motunde",
                Set.of());

        var order2 = Order.withRequiredFields(
                "2",
                SalesChannel.DELIVERY,
                Order.Status.PENDING,
                "", Order.PaymentStatus.PENDING,
                restaurantId.toString(),
                userId,
                "",
                Set.of());

        var order3 = Order.withRequiredFields(
                "3",
                SalesChannel.DELIVERY,
                Order.Status.DECLINED,
                "", Order.PaymentStatus.PENDING,
                restaurantId.toString(),
                userId,
                "",
                Set.of());

        var order4 = Order.withRequiredFields(
                "4",
                SalesChannel.DELIVERY,
                Order.Status.CONFIRMED,
                "", Order.PaymentStatus.PENDING,
                restaurantId.toString(),
                userId,
                "",
                Set.of());

        return orderSaveOperationsPort.saveOrders(List.of(
                order1,
                order2,
                order3,
                order4
        ));
    }

    @AfterEach
    void cleanup() {
        cleanupOrders();
    }
}
