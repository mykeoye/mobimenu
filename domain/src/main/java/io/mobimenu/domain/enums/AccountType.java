package io.mobimenu.domain.enums;

/**
 * Represents the user types on the system
 */
public enum AccountType {
    OWNER,         // The user who creates (owns) a restaurant on smart menu
    CUSTOMER,      // A user who created an account in a restaurant on smart menu
    EMPLOYEE,      // An employee at a restaurant in smart menu
    RESERVED       // This is account type is reserved for smart menu operations
}