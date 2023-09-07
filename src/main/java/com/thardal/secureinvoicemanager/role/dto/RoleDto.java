package com.thardal.secureinvoicemanager.role.dto;

import lombok.Data;

@Data
public class RoleDto {
    private Long id;
    private String roleName;
    private String permission;
}
