package io.mobimenu.core.service.user;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.user.CreateEmployeeUseCase;
import io.mobimenu.core.port.in.user.UpdateEmployeeUseCase;
import io.mobimenu.core.port.in.user.ViewEmployeeUseCase;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.domain.User;
import io.mobimenu.domain.enums.AccountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class EmployeeService implements CreateEmployeeUseCase, UpdateEmployeeUseCase, ViewEmployeeUseCase {

    private final UserQueryOperationsPort userQueryOperationsPort;
    private final UserSaveOperationsPort userSaveOperationsPort;

    @Override
    public Uni<User> createEmployee(CreateEmployeeCommand command) {
        var firstName = command.getFirstName().trim();
        var lastName = command.getLastName().trim();
        return userQueryOperationsPort.getByEmail(command.getEmail())
                .onItem().ifNotNull().failWith(Failure.of(Code.ACCOUNT_EXISTS))
                .flatMap(ignored -> userSaveOperationsPort.saveUser(
                        User.withEmployeeRequiredFields(
                                firstName,
                                lastName,
                                command.getEmail(),
                                command.getPhoneNumber(),
                                command.getRestaurantId(),
                                command.getEmployeeId(),
                                command.getTitle()
                        )
                ));
    }

    @Override
    public Uni<User> loadSingleEmployee(String id) {
        return userQueryOperationsPort.getById(id);
    }

    @Override
    public Uni<Tuple2<Long, List<User>>> loadAllEmployeesByRestaurant(String restaurantId, Page page) {
        return userQueryOperationsPort.getAllByRestaurantIdAndAccountType(restaurantId, AccountType.EMPLOYEE, page);
    }

    @Override
    public Uni<User> updateEmployeeDetails(UpdateEmployeeCommand command, String userId) {
        return userQueryOperationsPort.getById(userId)
                .onItem().ifNull().failWith(Failure.of(Code.USER_NOT_FOUND))
                .flatMap(emp -> userSaveOperationsPort.updateEmployee(
                        userId,
                        command.getFirstName(),
                        command.getLastName(),
                        command.getPhoneNumber(),
                        command.getAccountProperties()
                    )
                );
    }

    @Override
    public Uni<String> deleteEmployee(String userId) {
        return userQueryOperationsPort.getById(userId).onItem()
                .ifNull().failWith(Failure.of(Code.USER_NOT_FOUND))
                .flatMap(ignored -> userSaveOperationsPort.deleteUser(userId));
    }
}