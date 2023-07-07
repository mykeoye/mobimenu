package io.mobimenu.domain.filters;

import java.time.LocalDate;
import java.util.Objects;

public record DateRange(LocalDate from, LocalDate to) {

    public DateRange {
        if ((Objects.nonNull(from) && Objects.nonNull(to)) && from.isAfter(to)) {
            throw new IllegalArgumentException("from cannot be greater than to");
        }
    }

}
