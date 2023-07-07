package io.mobimenu.common.api;

import lombok.Getter;
import java.net.HttpURLConnection;

@Getter
public enum Code {

    GENERIC_SUCCESS("Operation successful", 3000, HttpURLConnection.HTTP_OK),
    BAD_REQUEST_CLIENT_ERROR("One or more parameters are invalid please check the request", 3001, HttpURLConnection.HTTP_BAD_REQUEST),
    AUTHENTICATION_ERROR("Invalid username and or password", 1000, HttpURLConnection.HTTP_UNAUTHORIZED),
    RESTAURANT_NOT_FOUND("No restaurant exists with the provided id", 2000, HttpURLConnection.HTTP_NOT_FOUND),
    SETTING_NOT_FOUND("No setting exists with the provided id", 2000, HttpURLConnection.HTTP_NOT_FOUND),
    PAYMENT_NOT_FOUND("No payment exists with the provided id", 2000, HttpURLConnection.HTTP_NOT_FOUND),
    MENU_NOT_FOUND("No menu exists with the provided id", 2001, HttpURLConnection.HTTP_NOT_FOUND),
    NO_ACTIVE_MENU("This restaurant has no active menu", 2001, HttpURLConnection.HTTP_BAD_REQUEST),
    MEALS_NOT_FOUND("One or more meals provided do not exists", 2003, HttpURLConnection.HTTP_NOT_FOUND),
    CATEGORY_NOT_FOUND("No category exists with the provided id", 2004, HttpURLConnection.HTTP_NOT_FOUND),
    ORDER_NOT_FOUND("No order exists with the provided id", 2005, HttpURLConnection.HTTP_NOT_FOUND),
    USER_NOT_FOUND("No user with provided email exists",2006, HttpURLConnection.HTTP_NOT_FOUND),
    QRCODE_NOT_FOUND("No qrcode with provided id exists",2006, HttpURLConnection.HTTP_NOT_FOUND),
    QRCODE_INACTIVE("The qrcode is inactive",2006, HttpURLConnection.HTTP_FORBIDDEN),
    CUSTOMER_NOT_FOUND("No customer with provided id exists",2006, HttpURLConnection.HTTP_NOT_FOUND),
    USER_EXISTS("A user with provided email exists",2006, HttpURLConnection.HTTP_NOT_FOUND),
    RESTAURANT_EXISTS("A restaurant with the same name already exists", 4001, HttpURLConnection.HTTP_CONFLICT),
    ORDER_PAYMENT_STATUS_UPDATED("The order payment status has already been updated to PAID", 4001, HttpURLConnection.HTTP_CONFLICT),
    CATEGORY_EXISTS("A category with the same name already exists", 4002, HttpURLConnection.HTTP_CONFLICT),
    QRCODE_EXISTS("A qrcode with the given number already exists", 4002, HttpURLConnection.HTTP_CONFLICT),
    TAG_EXISTS("A tag with the same name already exists", 4002, HttpURLConnection.HTTP_CONFLICT),
    MENU_EXISTS("A menu with the same name already exists", 4003, HttpURLConnection.HTTP_CONFLICT),
    MEAL_EXISTS("A meal with the same name already exists", 4003, HttpURLConnection.HTTP_CONFLICT),
    ACCOUNT_EXISTS("An account exists with the provided credentials", 4000, HttpURLConnection.HTTP_CONFLICT),
    GENERIC_ERROR("Something went wrong", 5000, HttpURLConnection.HTTP_INTERNAL_ERROR);

    private final int code;
    private final int httpCode;
    private final String message;

    Code(String message, int code, int httpCode) {
        this.message = message;
        this.code = code;
        this.httpCode = httpCode;
    }
}