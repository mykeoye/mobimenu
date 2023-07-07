package io.mobimenu.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.persistence.context.AbstractAdapterTest;
import io.mobimenu.persistence.context.TestObject;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class UserQueryAdapterTest extends AbstractAdapterTest {

    @Inject
    UserQueryAdapter userQueryAdapter;

    @Inject
    UserPersistenceAdapter userPersistenceAdapter;

    @Test
    void canFetchUserByEmail() {
        var testUser = TestObject.user();
        var created = userPersistenceAdapter.saveUser(testUser)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(created);

        var fetched = userQueryAdapter.getByEmail(testUser.getEmail())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetched);
        assertEquals(created.getUserId(), fetched.getUserId());
        assertEquals(testUser.getFirstName(), fetched.getFirstName());
        assertEquals(testUser.getAccountStatus(), fetched.getAccountStatus());
        assertEquals(testUser.getEmail(), fetched.getEmail());
        assertEquals(testUser.getLastName(), fetched.getLastName());
        assertEquals(testUser.getAccountType(), fetched.getAccountType());

        cleanupUser(created.getUserId());
    }

    @Test
    void canFetchUserByEmailAndAccountStatus() {
        var testUser = TestObject.user();
        var created = userPersistenceAdapter.saveUser(testUser)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(created);

        var fetched = userQueryAdapter.getByEmailAndAccountType(testUser.getEmail(), testUser.getAccountType())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetched);
        assertEquals(created.getUserId(), fetched.getUserId());
        assertEquals(testUser.getFirstName(), fetched.getFirstName());
        assertEquals(testUser.getAccountStatus(), fetched.getAccountStatus());
        assertEquals(testUser.getEmail(), fetched.getEmail());
        assertEquals(testUser.getLastName(), fetched.getLastName());
        assertEquals(testUser.getAccountType(), fetched.getAccountType());

        cleanupUser(created.getUserId());
    }

}