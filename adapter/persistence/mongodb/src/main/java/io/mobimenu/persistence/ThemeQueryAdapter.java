package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.out.theme.ThemeQueryOperationsPort;
import io.mobimenu.domain.Theme;
import io.mobimenu.persistence.mapper.ThemeMapper;
import io.mobimenu.persistence.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class implements all theme related queries to the datastore. Strictly retrievals only
 */
@RequiredArgsConstructor
public class ThemeQueryAdapter implements ThemeQueryOperationsPort {

    private final ThemeRepository repository;
    private final ThemeMapper mapper = ThemeMapper.INSTANCE;

    @Override
    public Uni<Theme> getById(Theme.ThemeId themeId) {
        return repository.findByThemeId(themeId.themeId())
                .onItem()
                .ifNotNull()
                .transform(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Tuple2<Long, List<Theme>>> getAllSystemDefined(Page page) {
        var countUni = repository.countAllSystemDefined();
        var themesUni = repository.findAllSystemDefined(page.getOffset(), page.getLimit())
                .map(themes -> Optional.ofNullable(themes).orElseGet(List::of))
                .map(themes -> themes.stream().map(mapper::entityToDomainObject).collect(Collectors.toList()));
        return Uni.combine().all().unis(countUni, themesUni).asTuple();
    }

}
