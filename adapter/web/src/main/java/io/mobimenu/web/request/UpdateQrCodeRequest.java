package io.mobimenu.web.request;

import io.mobimenu.domain.QrCode;
import javax.validation.constraints.NotNull;
import io.mobimenu.core.port.in.qrcode.UpdateQrCodeUseCase;

public record UpdateQrCodeRequest(

        @NotNull
        QrCode.Type type,

        @NotNull
        Integer number,

        @NotNull
        String restaurantId) {

    public UpdateQrCodeUseCase.UpdateQrCodeCommand toCommand(String qrCodeId) {
        return UpdateQrCodeUseCase.UpdateQrCodeCommand.builder()
                .number(number.toString())
                .restaurantId(restaurantId)
                .qrcodeId(qrCodeId)
                .type(type)
                .build();
    }
}
