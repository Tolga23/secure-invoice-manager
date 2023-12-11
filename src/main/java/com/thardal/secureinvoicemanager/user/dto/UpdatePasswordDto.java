package com.thardal.secureinvoicemanager.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDto {
    @NotEmpty(message = "Current password is required.")
    private String currentPassword;
    @NotEmpty(message = "New password is required.")
    private String newPassword;
    @NotEmpty(message = "Confirm password is required.")
    private String confirmPassword;
}
