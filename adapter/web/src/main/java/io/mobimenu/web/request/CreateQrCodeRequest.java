package io.mobimenu.web.request;

import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.qrcode.CreateQrCodeUseCase;
import io.mobimenu.domain.QrCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateQrCodeRequest(

        @NotNull
        QrCode.Type type,

        @NotNull
        QrCode.CreationType creationType,

        @NotBlank
        String restaurantId,

        String number,

        QrCode.Range range,

        String outletId) {

    public Uni<Failure> validate() {
        if (QrCode.CreationType.SINGLE.equals(creationType) && (number == null || number.isBlank())) {
            return Uni.createFrom().failure(Failure.of(Code.BAD_REQUEST_CLIENT_ERROR, "number is required for SINGLE creation type"));
        }
        if (QrCode.CreationType.BULK.equals(creationType) && (range == null || !range.isValid())) {
            return Uni.createFrom().failure(Failure.of(Code.BAD_REQUEST_CLIENT_ERROR, "the provided range is invalid"));
        }
        return Uni.createFrom().nullItem();
    }

    public CreateQrCodeUseCase.CreateQrCommand toCommand() {
        return CreateQrCodeUseCase.CreateQrCommand.builder()
                .creationType(creationType)
                .type(type)
                .number(number)
                .range(range)
                .restaurantId(restaurantId)
                .outletId(outletId)
                .build();
    }

}
