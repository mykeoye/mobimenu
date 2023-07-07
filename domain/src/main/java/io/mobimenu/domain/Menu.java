package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

/**
 * A menu is a collection of different meals. Restaurants can create as many menus as they need
 * but only one can be active at any given time.
 */
@Value
@ToString
@Builder
public class Menu {

    /**
     * A unique identifier for the menu
     */
    String menuId;
    /**
     * The title of the menu for easy identification
     */
    String name;

    /**
     * The menu's status, only one menu can be active at any given time. If a
     * status is not provided the default is INACTIVE
     */
    @Builder.Default
    Status status = Status.INACTIVE;

    /**
     * Theme holds customizable properties such as background image and colors for the menu
     */
    @JsonUnwrapped
    Theme theme;

    /**
     * The restaurant the menu belongs to. A menu is tied to exactly one restaurant
     */
    String restaurantId;

    /**
     * Creates a menu object with the minimum fields required to have a persistable entity
     *
     * @param name          the title (name) of the menu
     * @param status        the menu's status {@link Status}, defaults to inactive
     * @param theme         the menu's theme {@link Theme} which contains customizable properties
     * @param restaurantId  the restaurant {@link Restaurant} which the menu belongs to
     * @return              a menu object with the required fields initialized
     */
    public static Menu withRequiredFields(
            final String name,
            final Status status,
            final String restaurantId,
            final Theme theme) {

        return Menu.builder()
                .name(name)
                .status(status)
                .restaurantId(restaurantId)
                .theme(theme)
                .build();
    }

    /**
     * The menu's status. Only one menu can be active at any given time
     */
    public enum Status {
        ACTIVE, INACTIVE
    }

}
