package io.mobimenu.persistence.context;

import io.mobimenu.persistence.UserPersistenceAdapter;
import io.mobimenu.persistence.UserQueryAdapter;
import io.mobimenu.persistence.repository.UserRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class UserTestContext {

    @Produces
    @Singleton
    public UserPersistenceAdapter userPersistenceAdapter(UserRepository repository) {
        return new UserPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public UserQueryAdapter userQueryAdapter(UserRepository repository) {
        return new UserQueryAdapter(repository);
    }

}