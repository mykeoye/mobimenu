package io.mobimenu.core.port.out.qrcode;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.QrCode;
import java.util.List;

public interface QrCodeSaveOperationsPort {

    Uni<List<QrCode>> saveCodes(List<QrCode> qrCodes);

    Uni<QrCode> updateStatus(String restaurantId, QrCode.Status status, String qrcodeId);

    Uni<QrCode> update(String qrCodeId, QrCode.Type type, String number);

    Uni<QrCode> updateUrl(String qrCodeId, String url);

}
