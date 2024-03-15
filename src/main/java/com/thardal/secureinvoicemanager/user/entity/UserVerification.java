package com.thardal.secureinvoicemanager.user.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_VERIFICATION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVerification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String url;
}
