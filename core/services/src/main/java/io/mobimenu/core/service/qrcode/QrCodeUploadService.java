package io.mobimenu.core.service.qrcode;

import io.mobimenu.common.util.IdentifierProvider;
import io.mobimenu.common.util.QrCodeProvider;
import io.mobimenu.core.port.in.qrcode.UploadQrCodeUseCase;
import io.mobimenu.core.port.out.qrcode.QrCodeSaveOperationsPort;
import io.mobimenu.domain.QrCode;
import io.mobimenu.storage.StorageFolder;
import io.mobimenu.storage.StorageProvider;
import io.mobimenu.storage.StorageRequest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class QrCodeUploadService implements UploadQrCodeUseCase {

    private static final int QR_WIDTH = 525;
    private static final int QR_HEIGHT = 525;

    private final String qrMenuBaseUrl;
    private final StorageProvider storageProvider;
    private final QrCodeSaveOperationsPort qrCodeSaveOperationsPort;

    @Override
    public Uni<List<QrCode>> uploadCodes(List<QrCode> qrCodes) {
        return Uni.join().all(genQrImages(qrCodes)).andCollectFailures();
    }

    private List<Uni<QrCode>> genQrImages(List<QrCode> qrCodes) {
        return qrCodes.stream().map(qrCode -> createStorageRequest(qrCode.getQrCodeId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(tuple -> storageProvider.uploadFile(StorageFolder.QR_CODE, tuple.getItem2())
                .flatMap(storageResponse -> {
                    log.info("Uploaded QrImage for Qr: {} Storage response: {}", tuple.getItem1(), storageResponse);
                    return qrCodeSaveOperationsPort.updateUrl(tuple.getItem1(), storageResponse.url());
                }).log()).toList();
    }

    private Optional<Tuple2<String, StorageRequest>> createStorageRequest(String qrCodeId) {
        var qrText = "%s?qId=%s".formatted(qrMenuBaseUrl, qrCodeId);
        return Optional.ofNullable(QrCodeProvider.generateImage(qrText, QR_WIDTH, QR_HEIGHT))
                .map(image -> {
                    var imageBytes = Base64.getDecoder().decode(image);
                    return Tuple2.of(qrCodeId, new StorageRequest(IdentifierProvider.uuid("qr"),
                            new ByteArrayInputStream(imageBytes),
                            imageBytes.length, "image/png", null));
                });
    }

}
