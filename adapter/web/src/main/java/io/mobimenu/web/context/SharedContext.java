package io.mobimenu.web.context;

import io.mobimenu.common.security.DefaultPasswordSecurityProvider;
import io.mobimenu.common.security.PasswordSecurity;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class SharedContext {

    @Produces
    @Singleton
    public PasswordSecurity passwordSecurity() {
        return new DefaultPasswordSecurityProvider();
    }

}
