package com.thardal.secureinvoicemanager.user.entity;

import com.thardal.secureinvoicemanager.user.enums.EventType;
import jakarta.persistence.*;

@Entity
@Table(name = "EVENTS")
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "type", length = 60, nullable = false)
    @Enumerated(EnumType.STRING)
    EventType type;
    @Column(nullable = false)
    String description;
}
