package io.mobimenu.domain;

/**
 * Collection bank account for a restaurant
 *
 * @param bankAccountId     unique identifier for the bank account
 * @param accountName       the name on the account as provided by the bank
 * @param accountNumber     the account number
 * @param bank              the bank this account was issued
 * @param restaurantId      restaurant which owns this account
 */
public record BankAccount(String bankAccountId,
                          String accountName,
                          String accountNumber,
                          String bank,
                          String restaurantId) {

}
