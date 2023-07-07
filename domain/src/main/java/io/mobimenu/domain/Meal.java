package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.math.BigDecimal;
import java.util.Set;

/**
 * A meal is a food item that a restaurant offers on its menu. Meals can belong to more than
 * one menu. This makes it easier, to build menus with different combinations of meals
 */
@ToString
@Value
@Builder
public class Meal {

    /**
     * A unique identifier for the meal
     */
    @JsonUnwrapped
    MealId id;

    /**
     * The name of the meal
     */
    String name;

    /**
     * A set of menu ids {@link Menu} a meal belongs to
     */
    Set<String> menus;

    /**
     * A description of the meal, this can be a couple of sentences long
     */
    String description;

    /**
     * The meal images which will be displayed on the menu
     */
    Set<String> imageURIs;

    /**
     * URI of a 30s video of the meal
     */
    String videoURI;

    /**
     * The regular price is the normal selling price of the meal
     */
    BigDecimal normalPrice;

    /**
     * The availability status of the meal
     */
    @Builder.Default
    Status status = Status.AVAILABLE;

    /**
     * The menuId of the category {@link Category} the meal belongs to
     */
    @JsonUnwrapped
    Category.CategoryId categoryId;

    /**
     * The restaurant {@link Restaurant} the meal belongs to
     */
    String restaurantId;

    /**
     * The discount price is the price the meal is sold at for a discount
     */
    @Builder.Default
    BigDecimal discountPrice = BigDecimal.ZERO;

    /**
     * The cooking duration in minutes
     */
    long cookingDuration;

    /**
     * Creates a meal with all required fields
     *
     * @param name              the name of the meal
     * @param description       a description of the meal, this can be a couple of sentences long
     * @param menuIds           a set of menus (ids) {@link Menu} a meal belongs to
     * @param status            the availability status of the meal
     * @param imageURIs         the meal images which will be displayed on the menu
     * @param videoURI          URI of a 30s video of the meal
     * @param normalPrice       the regular price is the normal selling price of the meal
     * @param discountPrice     the discount price is the price the meal is sold at for a discount
     * @param cookingDuration   the cooking duration in minutes
     * @param restaurantId      the restaurant the meal belongs to
     *
     * @return                  a meal object with all fields initialized
     */
    public static Meal withRequiredFields(
            final String name,
            final String description,
            final Category.CategoryId categoryId,
            final Set<String> menuIds,
            final Status status,
            final Set<String> imageURIs,
            final String videoURI,
            final BigDecimal normalPrice,
            final BigDecimal discountPrice,
            final long cookingDuration,
            final String restaurantId) {

        return Meal.builder()
                .name(name)
                .description(description)
                .categoryId(categoryId)
                .menus(Set.copyOf(menuIds))
                .status(status)
                .imageURIs(imageURIs)
                .videoURI(videoURI)
                .normalPrice(normalPrice)
                .discountPrice(discountPrice)
                .cookingDuration(cookingDuration)
                .restaurantId(restaurantId)
                .build();
    }

    /**
     * Creates a meal with all fields
     *
     * @param id                a unique identifier for the meal
     * @param name              the name of the meal
     * @param description       a description of the meal, this can be a couple of sentences long
     * @param menuIds           the set of menus {@link Menu} a meal belongs to
     * @param status            the availability status of the meal
     * @param imageURIs         the meal images which will be displayed on the menu
     * @param videoURI          URI of a 30s video of the meal
     * @param normalPrice       the regular price is the normal selling price of the meal
     * @param discountPrice     the discount price is the price the meal is sold at for a discount
     * @param cookingDuration   the cooking duration in minutes
     * @param restaurantId      the restaurant the meal belongs to
     *
     * @return                  a meal object with all fields initialized
     */
    public static Meal withAllFields(
            final MealId id,
            final String name,
            final String description,
            final Category.CategoryId categoryId,
            final Set<String> menuIds,
            final Status status,
            final Set<String> imageURIs,
            final String videoURI,
            final BigDecimal normalPrice,
            final BigDecimal discountPrice,
            final long cookingDuration,
            final String restaurantId) {

        return Meal.builder()
                .id(id)
                .name(name)
                .description(description)
                .categoryId(categoryId)
                .menus(Set.copyOf(menuIds))
                .status(status)
                .imageURIs(Set.copyOf(imageURIs))
                .videoURI(videoURI)
                .normalPrice(normalPrice)
                .discountPrice(discountPrice)
                .cookingDuration(cookingDuration)
                .restaurantId(restaurantId)
                .build();
    }

    /**
     * The availability status of the meal
     */
    public enum Status {
        AVAILABLE, UNAVAILABLE
    }

    /**
     * Unique identifier for the meal
     */
    public record MealId(String mealId) {
    }

}
