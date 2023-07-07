package io.mobimenu.domain.filters;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import java.time.ZoneOffset;

/**
 * This filter is specific to filtering orders, it shouldn't be used for any query type
 */
@SuperBuilder
@Value
@EqualsAndHashCode(callSuper = true)
public class OrderFilter extends Filter {
    String restaurantId;
    String status;
    String paymentStatus;
    String customerId;

    public static OrderFilter from(String restaurantId, DateRange dateRange) {
        return OrderFilter.builder()
                .restaurantId(restaurantId)
                .from(dateRange.from() != null ? dateRange.from().atStartOfDay().toInstant(ZoneOffset.UTC) : null)
                .to(dateRange.to() != null ? dateRange.to().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusSeconds(1) : null)
                .build();
    }

    public static OrderFilter from(String restaurantId, String status, String paymentStatus) {
        return OrderFilter.builder()
                .restaurantId(restaurantId)
                .status(status)
                .paymentStatus(paymentStatus)
                .build();
    }

    public static OrderFilter from(String restaurantId, String status, String paymentStatus, String customerId, DateRange dateRange) {
        return OrderFilter.builder()
                .restaurantId(restaurantId)
                .status(status)
                .paymentStatus(paymentStatus)
                .customerId(customerId)
                .from(dateRange.from() != null ? dateRange.from().atStartOfDay().toInstant(ZoneOffset.UTC) : null)
                .to(dateRange.to() != null ? dateRange.to().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusSeconds(1) : null)
                .build();
    }

}
