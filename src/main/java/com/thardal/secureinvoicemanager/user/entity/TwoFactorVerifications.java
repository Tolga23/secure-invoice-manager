package com.thardal.secureinvoicemanager.user.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TwoFactorVerifications")
public class TwoFactorVerifications extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String verificationCode;
    private String expirationDate;
}
