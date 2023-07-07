package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Tag;
import io.mobimenu.persistence.mapper.TagMapper;
import io.mobimenu.persistence.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.out.tag.TagSaveOperationsPort;

@RequiredArgsConstructor
public class TagPersistenceAdapter implements TagSaveOperationsPort {

    private final TagRepository repository;
    private final TagMapper mapper = TagMapper.INSTANCE;

    @Override
    public Uni<Tag.TagId> saveTag(Tag tag) {
        var entity = mapper.domainObjectToPersistEntity(tag);
        return repository.persist(entity).map(persisted -> new Tag.TagId(persisted.id.toString()));
    }

}
