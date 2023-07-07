package io.mobimenu.domain.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Sales channels supported on the system
 */
public enum SalesChannel {

    DINE_IN("DINE-IN"),     // This is for dine in customers
    DELIVERY("DELIVERY"),   // For customers who placed an order from the website for delivery
    PICKUP("PICKUP");      // For customers who placed an order from the website for pickup

    private final String channel;

    SalesChannel(final String channel) {
        this.channel = channel;
    }

    /**
     * Get a human-readable name of the {@link SalesChannel}
     *
     * @return a human-readable string representation of the {@link SalesChannel}
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Gets the sales channel enum from a given string
     *
     * @param channel   the channel to map to a {@link SalesChannel}
     * @return          an optional containing the mapped enum or an empty optional if no mapping exists
     */
    public static Optional<SalesChannel> from(final String channel) {
        return Arrays.stream(SalesChannel.values())
                .filter(chan -> chan.channel.equals(channel.toUpperCase()))
                .findFirst();
    }

}
