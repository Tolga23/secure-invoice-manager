package com.thardal.secureinvoicemanager.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSettingsDto {
    @NotNull(message = "Enable cannot be null or empty")
    private Boolean enable;
    @NotNull(message = "IsNotLocked cannot be null or empty")
    private Boolean isNotLocked;
}
