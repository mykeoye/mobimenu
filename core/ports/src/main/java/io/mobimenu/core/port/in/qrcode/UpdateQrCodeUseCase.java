package io.mobimenu.core.port.in.qrcode;

import io.mobimenu.domain.QrCode;
import io.smallrye.mutiny.Uni;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

public interface UpdateQrCodeUseCase {

    Uni<QrCode> updateStatus(UpdateQrCodeStatusCommand command);

    Uni<QrCode> updateQrCode(UpdateQrCodeCommand command);

    @Value
    @Builder
    @ToString
    class UpdateQrCodeCommand {
        QrCode.Type type;
        String number;
        String qrcodeId;
        String restaurantId;
    }

    @Value
    @Builder
    @ToString
    class UpdateQrCodeStatusCommand {
        String restaurantId;
        QrCode.Status status;
        String outletId;
        String qrcodeId;
    }
}
