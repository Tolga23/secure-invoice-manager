package com.thardal.secureinvoicemanager.role.enums;

import com.thardal.secureinvoicemanager.base.enums.BaseErrorMessages;

public enum RoleErrorMessages implements BaseErrorMessages {

    ROLE_NOT_FOUND("No role found by name");
    private String message;

    RoleErrorMessages(String message) {
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
