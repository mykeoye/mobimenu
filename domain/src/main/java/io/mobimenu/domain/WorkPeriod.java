package io.mobimenu.domain;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Defines the operation hours for a given restaurant. It contains the opening and
 * closing hours as well as a grace extension period
 */
public record WorkPeriod (
         LocalTime openingHour,
         LocalTime closingHour,
         Duration gracePeriodInMinutes) {
}
