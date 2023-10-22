package com.thardal.secureinvoicemanager.user.enums;

import com.thardal.secureinvoicemanager.base.enums.BaseErrorMessages;

public enum UserErrorMessages implements BaseErrorMessages {
    USER_NOT_FOUND("User not found!"),
    EMAIL_ALREADY_IN_USE("Email already in use. Please use a different email."),
    BAD_CREDENTIALS("Incorrect email or password"),
    INCORRECT_EMAIL("Incorrect email"),
    CODE_EXPIRED("This code has expired. Please login again."),
    INVALID_CODE("Code is invalid");

    private String message;

    UserErrorMessages(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
