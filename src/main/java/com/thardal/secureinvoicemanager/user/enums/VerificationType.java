package com.thardal.secureinvoicemanager.user.enums;

public enum VerificationType {
    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type.toLowerCase();
    }

    public String toString() {
        return type;
    }
}
