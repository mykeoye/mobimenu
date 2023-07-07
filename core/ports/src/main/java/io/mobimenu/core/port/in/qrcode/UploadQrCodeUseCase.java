package io.mobimenu.core.port.in.qrcode;

import java.util.List;
import io.mobimenu.domain.QrCode;
import io.smallrye.mutiny.Uni;

public interface UploadQrCodeUseCase {

    Uni<List<QrCode>> uploadCodes(List<QrCode> qrCodes);

}
