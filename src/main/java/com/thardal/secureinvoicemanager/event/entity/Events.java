package com.thardal.secureinvoicemanager.event.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "EVENTS")
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "type", length = 60, nullable = false)
    String type;
    @Column(nullable = false)
    String description;

    // LOGIN_ATTEMPT, LOGIN_ATTEMPT_FAILURE, LOGIN_ATTEMPT_SUCCESS, ROLE_UPDATE, PASSWORD_UPDATE
    // PROFILE_UPDATE, PROFILE_PICTURE_UPDATE, MFA_UPDATE, ACCOUNT_SETTING_UPDATE
}
