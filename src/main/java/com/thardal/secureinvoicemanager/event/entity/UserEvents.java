package com.thardal.secureinvoicemanager.event.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "USER_EVENT")
@Data
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