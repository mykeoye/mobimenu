package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.math.BigDecimal;

@ToString
@Value
@Builder
public class OrderItem {

    /**
     * The name of the item
     */
    String name;

    /**
     * The cost of the item
     */
    BigDecimal price;

    /**
     * The quantity bought
     */
    long quantity;

    /**
     * Cooking duration
     */
    @JsonIgnore
    long cookingDurationInMin;

    /**
     * Creates an order item with the required fields
     *
     * @param name          the name of the item
     * @param price         the cost of the item
     * @param quantity      the quantity bought
     * @return              an order item
     */
    public static OrderItem withRequiredFields(
            String name,
            BigDecimal price,
            long quantity,
            long cookingDurationInMin) {

        return OrderItem.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .cookingDurationInMin(cookingDurationInMin)
                .build();
    }

}
