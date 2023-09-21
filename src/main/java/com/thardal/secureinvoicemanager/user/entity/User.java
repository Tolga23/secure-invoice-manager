package com.thardal.secureinvoicemanager.user.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


@Data
@Entity
@Table(name = "USERS")
@JsonInclude(NON_DEFAULT)
@Builder
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

    @Column(name = "IS_USİNG_AUTH")
    private boolean isUsingAuth;
}
