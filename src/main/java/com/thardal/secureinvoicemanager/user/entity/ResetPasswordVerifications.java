package com.thardal.secureinvoicemanager.user.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "ResetPasswordVerifications")
public class ResetPasswordVerifications extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String url;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
}
