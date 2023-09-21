package com.thardal.secureinvoicemanager.user.dto;

import lombok.Data;

@Data
public class UserVerificationSaveRequestDto {
    private Long userId;
    private String url;
}
