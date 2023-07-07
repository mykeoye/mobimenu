package io.mobimenu.domain.filters;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.time.Instant;

/**
 * This data-structure provides a base for the common fields all domain models can be filtered by
 */
@SuperBuilder
@Getter
public class Filter {
    Instant from;
    Instant to;
    Boolean deleted;
}
