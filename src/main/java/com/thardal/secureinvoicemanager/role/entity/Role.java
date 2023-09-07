package com.thardal.secureinvoicemanager.role.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ROLES")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID",nullable = false)
    private Long userId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "PERMISSION")
    private String permission;
}
