package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.mobimenu.domain.enums.SalesChannel;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Restaurant in the system which is a business entity that depending on its type {@link Restaurant.Type}
 * may or may not have outlets. A restaurant is said to have outlets if it spans more than a single geographical region
 */
@Value
@ToString
@Builder
public class Restaurant {

    /**
     * A unique identifier for the restaurant
     */
    String restaurantId;

    /**
     * The name of the restaurant or business. This is unique globally
     */
    String name;

    /**
     * The {@link Restaurant.Type } of restaurant business, can be a Single or Chain or outlets
     */
    Type type;

    /**
     * The owner account associated with this restaurant, this is the account that created or owns the restaurant
     */
    String ownerId;

    /**
     * The restaurant business email-address. This is not the same as the owner's account email
     **/
    @JsonIgnore
    String email;

    /**
     * Physical address for locating the restaurant
     */
    String address;

    /**
     * URI for locating the resource in the external storage service
     */
    String logoUri;

    /**
     * Channels the restaurant can receive orders from see {@link SalesChannel}. A restaurant may
     * have multiple sales channels but must have at least one sales channel
     */
    @Builder.Default
    Set<SalesChannel> salesChannels = new HashSet<>();

    /**
     * Business phone number {@link PhoneNumber}
     */
    @JsonIgnore
    PhoneNumber phoneNumber;

    /**
     * The restaurant's working hours {@link WorkPeriod} with a grace period. This is used to know
     * when the restaurant can accept orders and when it cannot
     */
    WorkPeriod workPeriod;

    /**
     * Theme holds customizable look and feel properties for the restaurant
     */
    @JsonUnwrapped
    Theme theme;

    /**
     * Creates a restaurant object with the minimum fields required for setting up an account
     * the object returned doesn't have an menuId. Since the menuId generation will happen on the database
     * layer
     *
     * @param name  the name of the restaurant
     * @param type  the type of restaurant business {@link Restaurant.Type}
     * @return      a restaurant object without an menuId
     */
    public static Restaurant withRequiredFields(final String name, final Restaurant.Type type) {
        return Restaurant.builder().name(name).type(type).build();
    }

    /**
     * Creates a restaurant object with all fields initialized. Useful for saturating domain objects with
     * data from the database
     *
     * @param restaurantId      a unique identifier for the restaurant
     * @param name              the name of the restaurant business
     * @param type              the type of restaurant business {@link Restaurant.Type}
     * @param email             business email for the restaurant
     * @param address           physical address of the restaurant
     * @param logoUri           uri for retrieving the restaurant's logo
     * @param salesChannels     channels the restaurant can receive orders from {@link SalesChannel}
     * @param phoneNumber       business phone number
     * @param workPeriod        the period the restaurant operates {@link WorkPeriod}
     * @return                  a restaurant object with all fields initialized
     */
    public static Restaurant withAllFields(
            final String restaurantId,
            final String name,
            final Type type,
            final String email,
            final String address,
            final String logoUri,
            final Set<SalesChannel> salesChannels,
            final PhoneNumber phoneNumber,
            final WorkPeriod workPeriod) {

        return Restaurant.builder()
                .restaurantId(restaurantId)
                .type(type)
                .name(name)
                .email(email)
                .address(address)
                .logoUri(logoUri)
                .salesChannels(salesChannels)
                .phoneNumber(phoneNumber)
                .workPeriod(workPeriod)
                .build();
    }

    /**
     * Determines if the restaurant is operational ie if it is within its defined working hours
     * by checking if the current time is withing the opening and closing hours (inclusive)
     * 
     * @param time  the local time we want to check the restaurant's operating period against
     * @return      true is yes false otherwise
     */
    public boolean isWithinWorkingPeriod(final LocalTime time) {
        return time.compareTo(workPeriod.openingHour()) >= 0
                && time.compareTo(workPeriod.closingHour()) <= 0;
    }

    /**
     * Determine if a restaurant can accept orders from the given sales channel
     *
     * @param channel   the sales channel {@link SalesChannel}
     * @return          true if it can, false otherwise
     */
    public boolean canAcceptOrderFromChannel(final SalesChannel channel) {
        return salesChannels.contains(channel);
    }

    /**
     * The restaurant business type. A single restaurant is one without any outlets ie a standalone
     * business in a single geographical location
     */
    public enum Type {
        SINGLE, CHAIN
    }

}
