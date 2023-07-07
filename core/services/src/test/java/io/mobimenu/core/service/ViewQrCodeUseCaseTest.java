package io.mobimenu.core.service;

import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.qrcode.ViewQrCodesUseCase;
import io.mobimenu.core.port.out.qrcode.QrCodeSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.QrCode;
import io.mobimenu.domain.filters.QrCodeFilter;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.util.List;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@QuarkusTest
public class ViewQrCodeUseCaseTest extends AbstractUseCaseTest {

    @Inject
    ViewQrCodesUseCase viewQrCodesUseCase;

    @Inject
    QrCodeSaveOperationsPort qrCodeSaveOperationsPort;

    @Test
    @DisplayName("When i fetch the list of qrcodes as a customer i should only get live codes")
    void fetchingListOfQrCodesShouldOnlyReturnLiveCodes() {
        var restaurantId = ObjectId.get().toString();
        var qrCodes = createQrCodes(restaurantId);
        assertEquals(3, qrCodes.size());

        var fetchedQrCodes = viewQrCodesUseCase.loadByFilter(QrCodeFilter.from(restaurantId, null), Page.of(1, 10))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem().getItem2();

        assertEquals(2, fetchedQrCodes.size());
        fetchedQrCodes.forEach(this::assertNoArchivedCodes);
    }

    void assertNoArchivedCodes(QrCode qrCode) {
        assertNotEquals(QrCode.Status.ARCHIVED, qrCode.getStatus());
    }

    List<QrCode> createQrCodes(String restaurantId) {
        return qrCodeSaveOperationsPort.saveCodes(List.of(
                QrCode.withAllFields(null, "4",
                        "https://s3.bucket.com/qr0983",
                        QrCode.Type.RESTAURANT,
                        QrCode.Status.ARCHIVED,
                        restaurantId),
                QrCode.withAllFields(null, "1",
                        "https://s3.bucket.com/qr0943",
                        QrCode.Type.HOTEL,
                        QrCode.Status.ACTIVE,
                        restaurantId),
                QrCode.withAllFields(null, "5",
                        "https://s3.bucket.com/qr1943",
                        QrCode.Type.HOTEL,
                        QrCode.Status.INACTIVE,
                        restaurantId)
        ))
        .subscribe().withSubscriber(UniAssertSubscriber.create())
        .awaitItem().assertCompleted().getItem();
    }

}
