package io.mobimenu.persistence;

import io.mobimenu.persistence.common.Fields;
import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.menu.MenuSaveOperationsPort;
import io.mobimenu.domain.Menu;
import io.mobimenu.persistence.mapper.MenuMapper;
import io.mobimenu.persistence.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements all menu persistence commands which include saves and updates
 */
@Slf4j
@RequiredArgsConstructor
public class MenuPersistenceAdapter implements MenuSaveOperationsPort {

    private final MenuRepository menuRepository;
    private final MenuMapper mapper = MenuMapper.INSTANCE;

    @Override
    public Uni<Menu> saveMenu(Menu menu) {
        // Since only one menu can be active at any given time we make all active menus inactive while creating a new one
        return menuRepository.update(Fields.STATUS, Menu.Status.INACTIVE).where(Fields.STATUS, Menu.Status.ACTIVE)
                .map(ignored -> mapper.domainObjectToPersistEntity(menu))
                .flatMap(entity -> menuRepository.persist(entity).map(mapper::entityToDomainObject));
    }
}
