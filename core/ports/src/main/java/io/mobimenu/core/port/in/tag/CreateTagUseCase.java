package io.mobimenu.core.port.in.tag;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Tag;
import lombok.Builder;
import lombok.Value;
import lombok.ToString;
import lombok.RequiredArgsConstructor;

public interface CreateTagUseCase {

    Uni<Tag.TagId> createTag(CreateTagCommand command);

    @Value
    @Builder
    @ToString
    @RequiredArgsConstructor
    class CreateTagCommand {
        String name;
        String iconURI;
        Boolean active;
        String restaurantId;
    }

}
