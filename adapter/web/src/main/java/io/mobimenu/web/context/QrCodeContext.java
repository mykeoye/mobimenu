package io.mobimenu.web.context;

import io.mobimenu.core.port.in.qrcode.CreateQrCodeUseCase;
import io.mobimenu.core.port.in.qrcode.UpdateQrCodeUseCase;
import io.mobimenu.core.port.in.qrcode.UploadQrCodeUseCase;
import io.mobimenu.core.port.in.qrcode.ViewQrCodesUseCase;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.qrcode.QrCodeQueryOperationsPort;
import io.mobimenu.core.port.out.qrcode.QrCodeSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.qrcode.QrCodeService;
import io.mobimenu.core.service.qrcode.QrCodeUploadService;
import io.mobimenu.persistence.QrCodePersistenceAdapter;
import io.mobimenu.persistence.QrCodeQueryAdapter;
import io.mobimenu.persistence.repository.QrCodeRepository;
import io.mobimenu.storage.StorageProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class QrCodeContext {

    @Produces
    @Singleton
    public QrCodeSaveOperationsPort qrCodeSaveOperationsPort(QrCodeRepository repository) {
        return new QrCodePersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public QrCodeQueryOperationsPort qrCodeQueryOperationsPort(QrCodeRepository repository) {
        return new QrCodeQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public CreateQrCodeUseCase createQrCodeUseCase(QrCodeSaveOperationsPort saveOperationsPort,
                                                   QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                                   RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                   MenuQueryOperationsPort menuQueryOperationsPort,
                                                   UploadQrCodeUseCase uploadQrCodeUseCase) {
        return qrCodeService(saveOperationsPort, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, uploadQrCodeUseCase);
    }

    @Singleton
    @Produces
    public ViewQrCodesUseCase viewQrCodesUseCase(QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                                 RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                 MenuQueryOperationsPort menuQueryOperationsPort) {
        return qrCodeService(null, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, null);
    }

    @Singleton
    @Produces
    public UpdateQrCodeUseCase updateQrCodeUseCase(QrCodeSaveOperationsPort qrCodeSaveOperationsPort,
                                                   QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                                   RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                   MenuQueryOperationsPort menuQueryOperationsPort,
                                                   UploadQrCodeUseCase uploadQrCodeUseCase) {
        return qrCodeService(qrCodeSaveOperationsPort, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, uploadQrCodeUseCase);
    }

    @Singleton
    @Produces
    public UploadQrCodeUseCase uploadQrCodeUseCase(StorageProvider storageProvider,
                                                   QrCodeSaveOperationsPort qrCodeSaveOperationsPort,
                                                   @ConfigProperty(name = "mobimenu.qrmenu.base.url") String baseUrl) {
        return new QrCodeUploadService(baseUrl, storageProvider, qrCodeSaveOperationsPort);
    }


    private QrCodeService qrCodeService(QrCodeSaveOperationsPort saveOperationsPort,
                                        QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                        RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                        MenuQueryOperationsPort menuQueryOperationsPort,
                                        UploadQrCodeUseCase uploadQrCodeUseCase) {
        return new QrCodeService(saveOperationsPort, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, uploadQrCodeUseCase);
    }
}
