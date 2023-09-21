package com.thardal.secureinvoicemanager.user.enums;

public enum RoleType {
    USER_ROLE("USER_ROLE"),
    ADMIN_ROLE("ADMIN_ROLE"),
    MANAGER_ROLE("MANAGER_ROLE"),
    SYSTEM_ADMIN_ROLE("SYSTEM_ADMIN_ROLE");

    private String roleName;
    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
