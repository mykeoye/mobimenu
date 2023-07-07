package io.mobimenu.core.service;

import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;
import io.mobimenu.core.port.in.qrcode.CreateQrCodeUseCase;
import io.mobimenu.core.port.in.qrcode.UpdateQrCodeUseCase;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.QrCode;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class UpdateQrCodeUseCaseTest extends AbstractUseCaseTest {

    @Inject
    UpdateQrCodeUseCase updateQrCodeUseCase;

    @Inject
    CreateQrCodeUseCase createQrCodeUseCase;

    @Inject
    CreateRestaurantUseCase createRestaurantUseCase;

    @Test
    @DisplayName("A merchant should be able to update a qrcode successfully")
    void asAUserIShouldBeAbleToUpdateMyQrCode() {
        var restaurant = createRestaurantUseCase.createRestaurant(createRestaurant("hooli@eats.com"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var qrCodes = createQrCodeUseCase.createQrCode(createQrCommand(restaurant.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(qrCodes);
        assertEquals(1, qrCodes.size());

        var qrCode = qrCodes.get(0);
        var updatedQrCode = updateQrCodeUseCase.updateQrCode(updateQrCodeCommand(restaurant.getRestaurantId(), qrCode.getQrCodeId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertEquals(qrCode.getQrCodeId(), updatedQrCode.getQrCodeId());
        assertEquals(QrCode.Type.HOTEL, updatedQrCode.getType());
        assertEquals("15", updatedQrCode.getTableNumber());

    }

    @Test
    @DisplayName("A merchant should not be able to update a qrcode to a type with the same number")
    void shouldNotBeAbleToUpdateQrCodeToATypeWithTheSameNumber() {
        var restaurant = createRestaurantUseCase.createRestaurant(createRestaurant("foodbank@gmail.com"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        createQrCodeUseCase.createQrCode(createQrCommand(restaurant.getRestaurantId(), QrCode.Type.RESTAURANT, "2"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();

        var qrCodes = createQrCodeUseCase.createQrCode(createQrCommand(restaurant.getRestaurantId(), QrCode.Type.HOTEL, "15"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(qrCodes);
        assertEquals(1, qrCodes.size());

        var qrCode = qrCodes.get(0);
        var throwable = updateQrCodeUseCase.updateQrCode(updateQrCodeCommand(restaurant.getRestaurantId(), qrCode.getQrCodeId(), QrCode.Type.RESTAURANT, "2"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();

        var failure = Failure.from(throwable);
        assertNotNull(failure);
        assertEquals(Code.QRCODE_EXISTS, failure.getError());

    }

    private CreateRestaurantUseCase.CreateRestaurantCommand createRestaurant(String email) {
        return CreateRestaurantUseCase.CreateRestaurantCommand.builder()
                .restaurantName("Hooli Eats")
                .emailAddress(email)
                .firstName("Owner")
                .lastName("Wales")
                .password("password")
                .build();
    }

    private CreateQrCodeUseCase.CreateQrCommand createQrCommand(String restaurantId, QrCode.Type type, String number) {
        return CreateQrCodeUseCase.CreateQrCommand.builder()
                .creationType(QrCode.CreationType.SINGLE)
                .restaurantId(restaurantId)
                .type(type)
                .number(number)
                .build();
    }

    private CreateQrCodeUseCase.CreateQrCommand createQrCommand(String restaurantId) {
        return CreateQrCodeUseCase.CreateQrCommand.builder()
                .creationType(QrCode.CreationType.SINGLE)
                .restaurantId(restaurantId)
                .type(QrCode.Type.RESTAURANT)
                .number("2")
                .build();
    }

    private UpdateQrCodeUseCase.UpdateQrCodeCommand updateQrCodeCommand(String restaurantId, String qrCodeId) {
        return UpdateQrCodeUseCase.UpdateQrCodeCommand.builder()
                .type(QrCode.Type.HOTEL)
                .number("15")
                .restaurantId(restaurantId)
                .qrcodeId(qrCodeId)
                .build();
    }

    private UpdateQrCodeUseCase.UpdateQrCodeCommand updateQrCodeCommand(String restaurantId, String qrCodeId, QrCode.Type type, String number) {
        return UpdateQrCodeUseCase.UpdateQrCodeCommand.builder()
                .type(type)
                .number(number)
                .restaurantId(restaurantId)
                .qrcodeId(qrCodeId)
                .build();
    }

    @AfterEach
    void cleanup() {
        cleanupRestaurant();
        cleanupQrCode();
    }

}
