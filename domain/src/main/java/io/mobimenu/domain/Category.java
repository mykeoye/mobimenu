package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * A category is a grouping of a set of meals {@link Meal}
 *
 * @param id            unique identifier for the category
 * @param name          name of the category. This name is distinct across a restaurant
 * @param status        the status of the category
 * @param restaurantId  the restaurant this category is associated with
 * @param isDefault     boolean value indicating if this category is the default category
 */
public record Category(
        @JsonUnwrapped
        CategoryId id,
        String name,
        Status status,
        String restaurantId,
        @JsonProperty("isDefault")
        boolean isDefault) {

    public Category(String name, Status status, String restaurantId, boolean isDefault) {
        this(null, name, status, restaurantId, isDefault);
    }

    /**
     * Creates a category with the required fields
     *
     * @param name          name of the category. This name is distinct across a restaurant
     * @param status        the status of the category
     * @param restaurantId  the restaurant this category is associated with
     * @param isDefault     boolean value indicating if this category is the default category
     * @return              a category object with required fields instantiated
     */
    public static Category withRequiredFields(
            String name,
            Status status,
            String restaurantId,
            boolean isDefault) {
        return new Category(null, name, status, restaurantId, isDefault);
    }

    /**
     * The status of the category
     */
    public enum Status {
        ACTIVE, INACTIVE
    }

    /**
     * Unique identifier for the category
     */
    public record CategoryId(String categoryId) {
    }

}
