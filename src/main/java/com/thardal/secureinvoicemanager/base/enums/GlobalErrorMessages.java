package com.thardal.secureinvoicemanager.base.enums;

public enum GlobalErrorMessages implements BaseErrorMessages{
    ERROR_OCCURRED("Something went wrong. Please try again later"),

    FILE_CANNOT_BE_SAVED("File cannot be saved. Please try again later");
    private String message;

    GlobalErrorMessages(String message){
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
