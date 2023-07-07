package io.mobimenu.domain;

/**
 * Represents a phone number with a countryCode and the line number
 */
public record PhoneNumber(String countryCode, String lineNumber) {

    @Override
    public String toString() {
        return "%s %s".formatted(countryCode, lineNumber).trim();
    }
}
