package io.mobimenu.domain.filters;

import io.mobimenu.domain.QrCode;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Value
@EqualsAndHashCode(callSuper = true)
public class QrCodeFilter extends Filter {
    String restaurantId;
    QrCode.Status status;

    public static QrCodeFilter from(String restaurantId, QrCode.Status status) {
        return QrCodeFilter.builder()
                .restaurantId(restaurantId)
                .status(status)
                .build();
    }
}
