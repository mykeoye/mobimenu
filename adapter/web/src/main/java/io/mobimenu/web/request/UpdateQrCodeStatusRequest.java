package io.mobimenu.web.request;

import io.mobimenu.core.port.in.qrcode.UpdateQrCodeUseCase;
import io.mobimenu.domain.QrCode;
import javax.validation.constraints.NotBlank;

public record UpdateQrCodeStatusRequest(

        @NotBlank
        String restaurantId,

        QrCode.Status status,

        String outletId) {

        public UpdateQrCodeUseCase.UpdateQrCodeStatusCommand toCommand(String qrCodeId) {
                return UpdateQrCodeUseCase.UpdateQrCodeStatusCommand.builder()
                        .restaurantId(restaurantId)
                        .status(status)
                        .outletId(outletId)
                        .qrcodeId(qrCodeId)
                        .build();
        }

}
