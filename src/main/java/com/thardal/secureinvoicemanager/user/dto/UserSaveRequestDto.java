package com.thardal.secureinvoicemanager.user.dto;

import lombok.Data;

@Data
public class UserSaveRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
}
