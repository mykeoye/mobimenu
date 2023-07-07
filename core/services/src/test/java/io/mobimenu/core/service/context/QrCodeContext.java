package io.mobimenu.core.service.context;

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
    public CreateQrCodeUseCase createQrCodeUseCase(QrCodeSaveOperationsPort qrCodeSaveOperationsPort,
                                                   QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                                   RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                   MenuQueryOperationsPort menuQueryOperationsPort,
                                                   UploadQrCodeUseCase uploadQrCodeUseCase) {
        return qrCodeService(qrCodeSaveOperationsPort, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, uploadQrCodeUseCase);
    }

    @Produces
    @Singleton
    public UpdateQrCodeUseCase updateQrCodeUseCase(QrCodeSaveOperationsPort qrCodeSaveOperationsPort,
                                                   QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                                   RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                   MenuQueryOperationsPort menuQueryOperationsPort) {
        return qrCodeService(qrCodeSaveOperationsPort, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, null);
    }

    @Produces
    @Singleton
    public ViewQrCodesUseCase viewQrCodesUseCase(QrCodeSaveOperationsPort qrCodeSaveOperationsPort,
                                                 QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                                 RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                 MenuQueryOperationsPort menuQueryOperationsPort) {
        return qrCodeService(qrCodeSaveOperationsPort, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, null);
    }

    @Singleton
    @Produces
    public UploadQrCodeUseCase uploadQrCodeUseCase(StorageProvider storageProvider,
                                                   QrCodeSaveOperationsPort qrCodeSaveOperationsPort,
                                                   @ConfigProperty(name = "mobimenu.qrmenu.base.url",defaultValue = "https://mobimenuap") String baseUrl) {
        return new QrCodeUploadService(baseUrl, storageProvider, qrCodeSaveOperationsPort);
    }


    private QrCodeService qrCodeService(QrCodeSaveOperationsPort qrCodeSaveOperationsPort,
                                        QrCodeQueryOperationsPort qrCodeQueryOperationsPort,
                                        RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                        MenuQueryOperationsPort menuQueryOperationsPort,
                                        UploadQrCodeUseCase uploadQrCodeUseCase) {
        return new QrCodeService(qrCodeSaveOperationsPort, qrCodeQueryOperationsPort, restaurantQueryOperationsPort, menuQueryOperationsPort, uploadQrCodeUseCase);
    }


}
