package com.thardal.secureinvoicemanager.user.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "USER_EVENTS")
public class UserEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    Long eventId;

    @Column(length = 100)
    String device;

    @Column(length = 100)
    String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;


}
