package com.thardal.secureinvoicemanager.event.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "USER_EVENT")
@Getter
@Setter
public class UserEvents extends BaseEntity {

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