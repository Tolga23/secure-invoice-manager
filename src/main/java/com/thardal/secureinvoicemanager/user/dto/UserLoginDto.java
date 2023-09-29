package com.thardal.secureinvoicemanager.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
