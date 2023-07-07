package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.core.port.in.user.CreateEmployeeUseCase;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.PhoneNumber;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CreateEmployeeUseCaseTest extends AbstractUseCaseTest {

    @Inject
    CreateEmployeeUseCase createEmployeeUseCase;

    @Inject
    UserQueryOperationsPort userQueryOperationsPort;

    @Test
    @DisplayName("Creating an employee with all invariants checkout out should be successful")
    void testToCreateAnEmployee() {
        var created = createEmployeeUseCase.createEmployee(createEmployeeCommand())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(created);
        assertNotNull(created.getUserId());

        var fetchedUser = userQueryOperationsPort.getById(created.getUserId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetchedUser);
        assertEquals(created.getUserId(), fetchedUser.getUserId());
    }

    CreateEmployeeUseCase.CreateEmployeeCommand createEmployeeCommand() {
        var phoneNumber = new PhoneNumber("+234", "9012345678");
        return CreateEmployeeUseCase.CreateEmployeeCommand.builder()
                .firstName("Kenny")
                .lastName("John")
                .email("myemail@eatery.com")
                .restaurantId(ObjectId.get().toHexString())
                .phoneNumber(phoneNumber)
                .employeeId("EAT11")
                .title("Mr")
                .password("password")
                .build();
    }

    @AfterEach
    void cleanup() {
        cleanupUser();
    }

}
