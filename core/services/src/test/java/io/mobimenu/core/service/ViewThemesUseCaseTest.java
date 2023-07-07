package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.theme.ViewThemesUseCase;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class ViewThemesUseCaseTest extends AbstractUseCaseTest {

    @Inject
    ViewThemesUseCase viewThemesUseCase;

    @Test
    @DisplayName("test that fetching non existent system themes returns an empty list")
    void testFetchingNonExistentSystemThemes() {
        var unis = viewThemesUseCase.loadSystemThemes(Page.of(1,10))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(unis);
        assertEquals(0, unis.getItem2().size());
    }
}
