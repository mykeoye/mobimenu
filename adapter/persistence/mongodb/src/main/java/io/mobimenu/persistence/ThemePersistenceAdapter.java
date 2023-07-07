package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.theme.ThemeSaveOperationsPort;
import io.mobimenu.domain.Theme;
import io.mobimenu.persistence.mapper.ThemeMapper;
import io.mobimenu.persistence.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;

/**
 * This class implements all theme persistence commands which include saves and updates
 */
@RequiredArgsConstructor
public class ThemePersistenceAdapter implements ThemeSaveOperationsPort {

    private final ThemeRepository repository;
    private final ThemeMapper mapper = ThemeMapper.INSTANCE;

    @Override
    public Uni<Theme.ThemeId> saveTheme(Theme theme) {
        var entity = mapper.domainObjectToPersistEntity(theme);
        return repository.persist(entity)
                .map(persisted -> new Theme.ThemeId(persisted.id.toString()));
    }

}
