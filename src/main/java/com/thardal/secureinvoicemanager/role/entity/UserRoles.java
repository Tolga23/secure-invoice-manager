package com.thardal.secureinvoicemanager.role.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "USER_ROLES")
public class UserRoles extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long roleId;
}
