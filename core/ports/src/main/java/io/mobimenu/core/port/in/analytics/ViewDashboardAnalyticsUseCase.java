package io.mobimenu.core.port.in.analytics;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.filters.DateRange;
import io.mobimenu.domain.Datum;

import java.util.List;

public interface ViewDashboardAnalyticsUseCase {

    Uni<List<Datum>> loadOrderSummaryByRange(String restaurantId, DateRange dateRange);

    Uni<List<Datum>> loadDashboardSummaryByRange(String restaurant, DateRange dateRange);

}
