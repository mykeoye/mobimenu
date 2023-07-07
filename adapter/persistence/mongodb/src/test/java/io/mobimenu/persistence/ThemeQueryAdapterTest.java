package io.mobimenu.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.enums.ThemeType;
import io.mobimenu.persistence.context.AbstractAdapterTest;
import io.mobimenu.persistence.context.TestObject;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class ThemeQueryAdapterTest extends AbstractAdapterTest {

    @Inject
    ThemeQueryAdapter themeQueryAdapter;

    @Inject
    ThemePersistenceAdapter themePersistenceAdapter;

    @Test
    void canFetchListOfSystemDefinedThemes() {
        var created = themePersistenceAdapter.saveTheme(TestObject.theme())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(created);

        var unis = themeQueryAdapter.getAllSystemDefined(Page.of(1, 20))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(unis);
        assertEquals(1, unis.getItem1());

        var fetched = unis.getItem2().get(0);
        assertEquals(created, fetched.id());
        assertEquals(ThemeType.SYSTEM, fetched.type());

        cleanup(fetched.id());

    }

    @Test
    void canFetchThemeById() {
        var created = themePersistenceAdapter.saveTheme(TestObject.theme())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(created);

        var theme = themeQueryAdapter.getById(created)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(theme);
        cleanup(created);

    }

}
