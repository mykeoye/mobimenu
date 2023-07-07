package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.mobimenu.domain.enums.ThemeType;

/**
 * A theme which contains customization information for domain objects that require theming
 */
public record Theme(
    @JsonUnwrapped
    ThemeId id,
    String name,
    String primaryColor,
    String accentColor,
    String logo,
    String backgroundImageURI,
    ThemeType type) {

    public Theme(String name, String primaryColor, String accentColor, String backgroundImageURI) {
        this(null, name, primaryColor, accentColor, null, backgroundImageURI, ThemeType.USER);
    }

    public Theme(String name, String primaryColor, String accentColor, String backgroundImageURI, ThemeType type) {
        this(null, name, primaryColor, accentColor, null, backgroundImageURI, type);
    }

    public Theme(String name, String primaryColor, String accentColor, String logo, String backgroundImageURI, ThemeType type) {
        this(null, name, primaryColor, accentColor, logo, backgroundImageURI, type);
    }

    public Theme(String logo, String primaryColor, String accentColor) {
        this(null, primaryColor, accentColor, logo, null, ThemeType.USER);
    }
    /**
     * A unique identifier for the Theme
     */
    public record ThemeId(String themeId) {
    }
}
