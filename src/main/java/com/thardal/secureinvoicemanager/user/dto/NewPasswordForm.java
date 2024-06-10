package com.thardal.secureinvoicemanager.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordForm {
    @NotNull(message = "User id cannot be null.")
    private Long userId;
    @NotNull(message = "Password cannot be empty.")
    private String password;
    @NotNull(message = "Confirm password cannot be empty.")
    private String confirmPassword;
}
