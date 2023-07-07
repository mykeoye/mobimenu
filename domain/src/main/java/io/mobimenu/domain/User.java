package io.mobimenu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.mobimenu.domain.enums.AccountProp;
import io.mobimenu.domain.enums.AccountStatus;
import io.mobimenu.domain.enums.AccountType;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a user on the system
 */
@Value
@ToString
@Builder
public class User {

    /**
     * Unique identifier for the user
     */
    String userId;

    /**
     * The user's first name
     */
    String firstName;

    /**
     * The user's last name
     */
    String lastName;

    /**
     * The user's phone number this field is optional
     */
    @JsonIgnore
    PhoneNumber phoneNumber;

    /**
     * The user's email address
     */
    @JsonIgnore
    String email;

    /**
     * Indicates if the user has been verified or not
     */
    boolean verified;

    /**
     * The type of account the user has
     */
    AccountType accountType;

    /**
     * The user's password
     */
    @JsonIgnore
    String password;

    /**
     * The status of the user's account
     */
    @Builder.Default
    AccountStatus accountStatus = AccountStatus.ACTIVE;

    /**
     * The restaurants tied to the user
     */
    @Builder.Default
    Set<String> restaurants = new HashSet<>();

    /**
     * The user's account properties
     */
    @Builder.Default
    Map<String, String> accountProperties = new HashMap<>();

    /**
     * Create a user object with all required fields. Useful for persisting domain entities
     *
     * @param firstName      the user's firstname
     * @param lastName       the user's lastname
     * @param emailAddress   the user's email address
     * @param type           the user's account type
     *
     * @return               a user object with required fields initialized
     */
    public static User withRequiredFields(
            String firstName,
            String lastName,
            String emailAddress,
            AccountType type,
            String password) {

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(emailAddress)
                .accountType(type)
                .password(password)
                .build();
    }

    /**
     * Create a user object with all fields. Useful for persisting domain entities
     *
     * @param firstName      the user's firstname
     * @param lastName       the user's lastname
     * @param emailAddress   the user's email address
     * @param type           the user's account type
     *
     * @return               a user object with required fields initialized
     */
    public static User withAllFields(
            String userId,
            String firstName,
            String lastName,
            String emailAddress,
            AccountType type,
            boolean verified,
            PhoneNumber phoneNumber,
            String password,
            AccountStatus accountStatus) {

        return User.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .email(emailAddress)
                .accountType(type)
                .verified(verified)
                .phoneNumber(phoneNumber)
                .password(password)
                .accountStatus(accountStatus)
                .build();
    }

    /**
     * Creates a user of type CUSTOMER, with the fields required for customers
     *
     * @param firstName     the customer's firstname
     * @param lastName      the customer's lastname
     * @param email         the customer's email address
     * @param phoneNumber   the customer's phone number
     *
     * @return              a user of type customer with required fields initialized
     */
    public static User withCustomerFields(
            String firstName,
            String lastName,
            String email,
            PhoneNumber phoneNumber,
            Set<String> restaurants) {

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .accountType(AccountType.CUSTOMER)
                .password(null)
                .restaurants(Set.copyOf(restaurants))
                .accountProperties(Map.of(
                    AccountProp.TOTAL_ORDERS, "0.00",
                    AccountProp.TOTAL_SPEND, "0.00"))
                .build();

    }


    /**
     * Create a user object of type EMPLOYEE with the EMPLOYEE required fields
     *
     * @param firstName         the employee's firstname
     * @param lastName          the employee's lastname
     * @param email             the employee's email address
     * @param phoneNumber       the employee's phoneNumber
     * @param restaurantId      the restaurant the employee will be registered to
     * @param employeeId        the employee's staffId
     * @param title             the title the employee is addresses as
     *
     * @return                  a user of type EMPLOYEE with required fields initialized
     */
    public static User withEmployeeRequiredFields(
            String firstName,
            String lastName,
            String email,
            PhoneNumber phoneNumber,
            String restaurantId,
            String employeeId,
            String title) {

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .accountType(AccountType.EMPLOYEE)
                .restaurants(Set.of(restaurantId))
                .accountProperties(Map.of(AccountProp.EMPLOYEE_ID, employeeId, AccountProp.TITLE, title))
                .build();
    }


}