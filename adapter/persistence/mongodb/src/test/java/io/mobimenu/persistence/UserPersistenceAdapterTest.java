package io.mobimenu.persistence;

import javax.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.persistence.context.AbstractAdapterTest;
import io.mobimenu.persistence.context.TestObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class UserPersistenceAdapterTest extends AbstractAdapterTest {

    @Inject
    UserPersistenceAdapter userPersistenceAdapter;

    @Test
    void canCreateAUserSuccessfully() {
        var testUser = TestObject.user();
        var created = userPersistenceAdapter.saveUser(testUser)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(created);
        assertNotNull(created.getUserId());
        cleanupUser(created.getUserId());
    }

}
