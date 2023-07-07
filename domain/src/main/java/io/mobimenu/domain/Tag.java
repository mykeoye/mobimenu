package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents a tag which allows the user group items, it works like a category but is more flexible
 *
 * @param id            unique identifier for the menuId
 * @param name          name of the tag it is unique per {@link Restaurant}
 * @param icon          an icon for the tag
 * @param status        tag status active or not
 * @param restaurantId  the restaurant this tag belongs to
 */
public record Tag (@JsonUnwrapped TagId id, String name, String icon, Status status, String restaurantId) {


    /**
     * Creates a tag object with required fields, useful for saturating persistence entities
     *
     * @param name              name of the tag it is unique per {@link Restaurant}
     * @param icon              an icon for the tag
     * @param status            tag status active or not
     * @param restaurantId      the restaurant this tag belongs to
     * @return                  a tag object with required fields
     */
    public static Tag withRequiredFields(String name,
                                         String icon,
                                         Status status,
                                         String restaurantId) {
        return new Tag(null, name, icon, status, restaurantId);
    }

    /**
     * Creates a tag object with required all fields initialized
     *
     * @param tagId             Unique identifier for the tag
     * @param name              name of the tag it is unique per {@link Restaurant}
     * @param icon              an icon for the tag
     * @param status            tag status active or not
     * @param restaurantId      the restaurant this tag belongs to
     * @return                  a tag object with required fields
     */
    public static Tag withRequiredFields(TagId tagId,
                                         String name,
                                         String icon,
                                         Status status,
                                         String restaurantId) {
        return new Tag(null, name, icon, status, restaurantId);
    }

    public enum Status {
        ACTIVE, INACTIVE
    }

    /**
     * Unique identifier for the tag
     */
    public record TagId(String tagId) {
    }
}
