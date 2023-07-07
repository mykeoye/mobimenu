package io.mobimenu.web.context;

import io.mobimenu.core.port.in.analytics.ViewDashboardAnalyticsUseCase;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.service.analytics.AnalyticsService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class AnalyticsContext {

    @Produces
    @Singleton
    public ViewDashboardAnalyticsUseCase viewDashboardAnalyticsUseCase(OrderQueryOperationsPort orderQueryOperationsPort,
                                                                       UserQueryOperationsPort userQueryOperationsPort) {
        return new AnalyticsService(orderQueryOperationsPort, userQueryOperationsPort);
    }
}
