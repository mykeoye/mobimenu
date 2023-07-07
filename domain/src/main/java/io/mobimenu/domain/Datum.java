package io.mobimenu.domain;

/**
 * Data item representation for the dashboard ie the set of order metric cards
 *
 * @param type      the type of data to display
 * @param value     the value of the data
 */
public record Datum(Datum.Type type, String value) {

    public enum Type {
        TOTAL_ORDERS, TOTAL_SALES, PENDING_ORDERS, ACCEPTED_ORDERS, DECLINED_ORDERS, TOTAL_CUSTOMERS;
    }

}
