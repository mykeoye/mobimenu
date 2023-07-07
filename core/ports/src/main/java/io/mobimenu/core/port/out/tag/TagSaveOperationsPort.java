package io.mobimenu.core.port.out.tag;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Tag;

public interface TagSaveOperationsPort {

    Uni<Tag.TagId> saveTag(Tag tag);

}
