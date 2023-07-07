package io.mobimenu.domain;

import io.mobimenu.domain.enums.SalesChannel;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

/**
 * An order is a request for a meal or a set of meals, made from the diner interface or
 * the website. Orders contain one or more meal items and can be accepted or rejected by
 * the restaurant admin.
 */
@ToString
@Value
@Builder
public class Order {

    private static final BigDecimal VAT = new BigDecimal("0.075");

    /**
     * Unique identifier for the order
     */
    String orderId;

    /**
     * A human-readable representation that can be used to quickly identify an order. This is incremental
     * and unique per restaurant. ie every order has a unique order number
     */
    String orderNum;

    /**
     * The total cost of the order. This is derived from computing the sum of the individual prices of
     * all the items that make up the order
     */
    BigDecimal totalCost;

    /**
     * Number of items that make up the order
     */
    long numOfItems;

    /**
     * The wait time in minutes
     */
    @Builder.Default
    long waitTimeInMinutes = 30;

    /**
     * This indicates the channel the order came through
     */
    SalesChannel salesChannel;

    /**
     * The status of the order
     */
    Status status;

    /**
     * The payment status of the order
     */
    PaymentStatus paymentStatus;

    /**
     * The table or room number the order came from, this only applies to Dine in orders
     */
    String tableNumber;

    /**
     * The customer who placed the order, this is so we track customers who have an account. And have placed
     * and order on the platform. This field is nullable
     */
    String customerId;

    /**
     * This holds the name of the customer at order placement. Can be a guest user or a user with an account
     */
    String customerName;

    /**
     * The restaurant the order is originating from
     */
    String restaurantId;

    /**
     * The items that make up the order
     */
    Set<OrderItem> items;

    /**
     * Value added tax mandated by law to be collected by businesses. This values doesn't change
     * or is not intended to change. But it should be read from a backing store.
     */
    BigDecimal vatAmount;

    /**
     * Delivery fee for cases when the order is placed for delivery
     */
    @Builder.Default
    BigDecimal deliveryFee = BigDecimal.ZERO;

    /**
     * Create an order with required fields
     *
     * @param salesChannel          the channel the order originated from
     * @param status                the status of the order
     * @param paymentStatus         the payment status of the order either paid or unpaid
     * @return                      an order object
     */
    public static Order withRequiredFields(
            String orderNum,
            SalesChannel salesChannel,
            Status status,
            String table,
            PaymentStatus paymentStatus,
            String restaurantId,
            String customerId,
            String customerName,
            Set<OrderItem> items) {

        var orderItems = Set.copyOf(items);
        return Order.builder()
                .orderNum(orderNum)
                .totalCost(computeTotalCost(orderItems, BigDecimal.ZERO))
                .vatAmount(computeVatAmount(computeTotalPrice(orderItems)))
                .restaurantId(restaurantId)
                .numOfItems(computeNumberOfItems(orderItems))
                .status(status)
                .waitTimeInMinutes(computeWaitTime(orderItems))
                .tableNumber(table)
                .paymentStatus(paymentStatus)
                .salesChannel(salesChannel)
                .customerId(customerId)
                .customerName(customerName)
                .items(orderItems)
                .build();
    }

    /**
     * Create an order with required fields
     *
     * @param id                    unique identifier for the order
     * @param salesChannel          the channel the order originated from
     * @param status                the status of the order
     * @param paymentStatus         the payment status of the order either paid or unpaid
     * @param waitTimeInMinutes     the total time it takes to complete the order (estimated)
     * @return                      an order object
     */
    public static Order withAllFields(
            String id,
            String orderNum,
            SalesChannel salesChannel,
            Status status,
            long waitTimeInMinutes,
            PaymentStatus paymentStatus,
            String restaurantId,
            Set<OrderItem> items) {

        var orderItems = Set.copyOf(items);
        return Order.builder()
                .orderId(id)
                .orderNum(orderNum)
                .totalCost(computeTotalCost(orderItems, BigDecimal.ZERO))
                .vatAmount(computeVatAmount(computeTotalPrice(orderItems)))
                .restaurantId(restaurantId)
                .numOfItems(computeNumberOfItems(orderItems))
                .waitTimeInMinutes(computeWaitTime(orderItems))
                .status(status)
                .waitTimeInMinutes(waitTimeInMinutes)
                .paymentStatus(paymentStatus)
                .salesChannel(salesChannel)
                .items(orderItems)
                .build();
    }

    /**
     * Computes the total number of items
     *
     * @param items     the items that make up the order
     * @return          the total items in the order (sum of the individual quantities)
     */
    private static long computeNumberOfItems(Set<OrderItem> items) {
        return items.size();
    }

    /**
     * Computes the total price of the items that make up the order
     *
     * @param items     the items that make up the order
     * @return          the total price of the items in the order
     */
    private static BigDecimal computeTotalPrice(Set<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);

    }

    /**
     * Calculates the total cost of the order. This includes vat and delivery fee
     *
     * @param orderItems    the items to compute the total cost for
     * @param deliveryFee   amount the customer has to pay for delivery. Only applies to DELIVERY type orders
     * @return              total cost of the order
     */
    private static BigDecimal computeTotalCost(Set<OrderItem> orderItems, BigDecimal deliveryFee) {
        var totalPrice = computeTotalPrice(orderItems);
        return totalPrice.add(computeVatAmount(totalPrice)).add(deliveryFee).setScale(2, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal computeVatAmount(BigDecimal totalPrice) {
        return totalPrice.multiply(VAT).setScale(2, RoundingMode.HALF_EVEN);
    }

    private static long computeWaitTime(Set<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::getCookingDurationInMin)
                .reduce(0L, Long::sum);
    }

    /**
     * The status of the order as it transitions from one state to the next
     */
    public enum Status {
        PENDING, CONFIRMED, DECLINED, PROCESSING, PROCESSED, DELIVERED
    }

    /**
     * Payment status of the order
     */
    public enum PaymentStatus {
        PAID, PENDING
    }

}
