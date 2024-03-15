package com.thardal.secureinvoicemanager.user.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Entity
@Table(name = "USERS")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "User", sequenceName = "USER_ID_SEQ")
    @GeneratedValue(generator = "User")
    private Long id;

    @NotEmpty(message = "First name cannot be empty")
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "BIO")
    private String bio;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "ENABLE")
    private boolean enable;

    @Column(name = "IS_NOT_LOCKED")
    private boolean isNotLocked;

    @Column(name = "IS_USING_AUTH")
    private boolean isUsingAuth;
}
