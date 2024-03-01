package com.thardal.secureinvoicemanager.event.dto;

import lombok.Data;

@Data
public class UserEventDetailsDto {
    private String device;
    private String ipAddress;
    private String eventType;
    private String description;
    private String createdAt;
}
