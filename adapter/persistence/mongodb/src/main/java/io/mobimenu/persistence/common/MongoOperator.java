package io.mobimenu.persistence.common;

/**
 * Helper class with static fields containing commonly used mongodb operators
 */
public final class MongoOperator {
    public static final String GTE = "$gte";
    public static final String LTE = "$lte";
    public static final String AND = "$and";
    public static final String SUM = "$sum";
    public static final String IN = "$in";
    public static final String PROJECTION = "$projection";
}
