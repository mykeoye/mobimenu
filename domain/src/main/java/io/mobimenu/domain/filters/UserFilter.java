package io.mobimenu.domain.filters;

import io.mobimenu.domain.enums.AccountType;
import lombok.EqualsAndHashCode;
import lombok.Value;
import java.time.ZoneOffset;
import lombok.experimental.SuperBuilder;

/**
 * This filter is specific to filtering users, it shouldn't be used for any query type
 */
@SuperBuilder
@Value
@EqualsAndHashCode(callSuper = true)
public class UserFilter extends Filter {
    AccountType accountType;
    String userId;
    String restaurantId;

    public static UserFilter from (String userId, String restaurantId, AccountType accountType) {
        return UserFilter.builder()
                .userId(userId)
                .restaurantId(restaurantId)
                .accountType(accountType)
                .build();
    }

    public static UserFilter from(String userId, String restaurantId, AccountType accountType, DateRange dateRange) {
        return UserFilter.builder()
                .userId(userId)
                .restaurantId(restaurantId)
                .accountType(accountType)
                .from(dateRange.from() != null ? dateRange.from().atStartOfDay().toInstant(ZoneOffset.UTC) : null)
                .to(dateRange.to() != null ? dateRange.to().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusSeconds(1) : null)
                .build();
    }
}
