package io.mobimenu.core.service;

import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.qrcode.CreateQrCodeUseCase;
import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.QrCode;
import io.mobimenu.domain.Restaurant;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CreateQrCodeUseCaseTest extends AbstractUseCaseTest {

    @Inject
    CreateQrCodeUseCase createQrCodeUseCase;

    @Inject
    CreateRestaurantUseCase createRestaurantUseCase;

    @Test
    @DisplayName("Creating a qr-code should be successful")
    void asACustomerIShouldBeAbleToSuccessfullyCreateAQrCode() {
        var restaurant = createRestaurant();
        var qrCodes = createQrCode(createSingleQrCommand(restaurant.getRestaurantId()));
        assertNotNull(qrCodes);
        assertFalse(qrCodes.isEmpty());
        assertEquals(1, qrCodes.size());

        var qrCode = qrCodes.get(0);
        assertEquals("2", qrCode.getTableNumber());
        assertNotNull(qrCode.getQrCodeId());
        assertNotNull(qrCode.getType());
        assertNotNull(qrCode.getStatus());
        assertNotNull(qrCode.getQrImageUri());
    }

    @Test
    @DisplayName("A user should be able to create qr codes in bulk")
    void aCustomerShouldBeAbleToCreateBulkCodes() {
        var restaurant = createRestaurant();
        var qrCodes = createQrCode(createBulkQrCommand(restaurant.getRestaurantId()));
        assertNotNull(qrCodes);
        assertFalse(qrCodes.isEmpty());
        assertEquals(5, qrCodes.size());
    }

    @Test
    @DisplayName("A user should not be able to create a qr code with an existing table number")
    void asACustomerIShouldNotBeAbleToCreateAQrCodeWithAnExistingTableNumber() {
        var restaurant = createRestaurant();
        var qrCodes = createQrCode(createSingleQrCommand(restaurant.getRestaurantId()));
        assertNotNull(qrCodes);
        assertFalse(qrCodes.isEmpty());

        var throwable = createQrCodeUseCase.createQrCode(createSingleQrCommand(restaurant.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();

        var failure = Failure.from(throwable);
        assertEquals(Code.QRCODE_EXISTS, failure.getError());
    }

    private Restaurant createRestaurant() {
        return createRestaurantUseCase.createRestaurant(createRestaurantCommand())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
    }

    private List<QrCode> createQrCode(CreateQrCodeUseCase.CreateQrCommand command) {
        return createQrCodeUseCase.createQrCode(command)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
    }

    private CreateRestaurantUseCase.CreateRestaurantCommand createRestaurantCommand() {
        return CreateRestaurantUseCase.CreateRestaurantCommand.builder()
                .restaurantName("Hooli Eats")
                .emailAddress("hooli@eats.com")
                .firstName("Owner")
                .lastName("Wales")
                .password("password")
                .build();
    }

    private CreateQrCodeUseCase.CreateQrCommand createSingleQrCommand(String restaurantId) {
        return CreateQrCodeUseCase.CreateQrCommand.builder()
                .creationType(QrCode.CreationType.SINGLE)
                .restaurantId(restaurantId)
                .type(QrCode.Type.RESTAURANT)
                .number("2")
                .build();
    }

    private CreateQrCodeUseCase.CreateQrCommand createBulkQrCommand(String restaurantId) {
        return CreateQrCodeUseCase.CreateQrCommand.builder()
                .creationType(QrCode.CreationType.BULK)
                .restaurantId(restaurantId)
                .type(QrCode.Type.RESTAURANT)
                .range(new QrCode.Range(1, 5))
                .build();
    }

    @AfterEach
    void cleanup() {
        cleanupUser();
        cleanupRestaurant();
        cleanupQrCode();
    }
}
