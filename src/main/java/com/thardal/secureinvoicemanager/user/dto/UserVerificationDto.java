package com.thardal.secureinvoicemanager.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVerificationDto {
    private Long id;
    private Long userId;
    private String url;
}
