package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.user.CreateEmployeeUseCase;
import io.mobimenu.core.port.in.user.UpdateEmployeeUseCase;
import io.mobimenu.core.port.in.user.UpdateUserProfileUseCase;
import io.mobimenu.core.port.in.user.ViewEmployeeUseCase;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.core.service.UserProfileService;
import io.mobimenu.core.service.user.EmployeeService;
import io.mobimenu.persistence.UserPersistenceAdapter;
import io.mobimenu.persistence.UserQueryAdapter;
import io.mobimenu.persistence.repository.UserRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class UserContext {

    @Produces
    @Singleton
    public UserQueryOperationsPort userQueryOperationsPort(UserRepository repository) {
        return new UserQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public UserSaveOperationsPort userSaveOperationsPort(UserRepository userRepository) {
        return new UserPersistenceAdapter(userRepository);
    }

    @Produces
    @Singleton
    public ViewEmployeeUseCase viewEmployeeUseCase(UserQueryOperationsPort userQueryOperationsPort,
                                                   UserSaveOperationsPort userSaveOperationsPort) {
        return employeeService(userQueryOperationsPort, userSaveOperationsPort);
    }

    @Produces
    @Singleton
    public CreateEmployeeUseCase createEmployeeUseCase(UserQueryOperationsPort userQueryOperationsPort,
                                                   UserSaveOperationsPort userSaveOperationsPort) {
        return employeeService(userQueryOperationsPort, userSaveOperationsPort);
    }

    @Produces
    @Singleton
    public UpdateEmployeeUseCase updateEmployeeUseCase(UserQueryOperationsPort userQueryOperationsPort,
                                                       UserSaveOperationsPort userSaveOperationsPort) {
        return employeeService(userQueryOperationsPort, userSaveOperationsPort);
    }

    @Produces
    @Singleton
    public UpdateUserProfileUseCase updateUserProfileUseCase(UserQueryOperationsPort userQueryOperationsPort,
                                                             UserSaveOperationsPort userSaveOperationsPort) {
        return new UserProfileService(userQueryOperationsPort, userSaveOperationsPort);
    }

    EmployeeService employeeService(UserQueryOperationsPort userQueryOperationsPort,
                                    UserSaveOperationsPort userSaveOperationsPort) {
        return new EmployeeService(userQueryOperationsPort, userSaveOperationsPort);
    }

}
