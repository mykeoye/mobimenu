package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Tag;
import lombok.RequiredArgsConstructor;
import io.mobimenu.persistence.mapper.TagMapper;
import io.mobimenu.persistence.repository.TagRepository;
import io.mobimenu.core.port.out.tag.TagQueryOperationsPort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TagQueryAdapter implements TagQueryOperationsPort {

    private final TagRepository repository;
    private final TagMapper mapper = TagMapper.INSTANCE;

    @Override
    public Uni<Tag> getById(Tag.TagId tagId) {
        return repository.findByTagId(tagId.tagId()).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Tag> getByNameAndRestaurant(String name, String restaurantId) {
        return repository.findByNameAndRestaurant(name, restaurantId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Tuple2<Long, List<Tag>>> getTagsByRestaurant(String restaurantId, Page page) {
        var countUni = repository.countAllByRestaurant(restaurantId);
        var tagsUni = repository.findAllByRestaurant(restaurantId, page.getOffset(), page.getLimit())
                .map(tags -> Optional.ofNullable(tags).orElseGet(List::of))
                .map(themes -> themes.stream().map(mapper::entityToDomainObject).collect(Collectors.toList()));
        return Uni.combine().all().unis(countUni, tagsUni).asTuple();
    }

}
