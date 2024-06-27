package com.thardal.secureinvoicemanager.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceNumber;
    private String services;
    private String status;
    private Double total;

    /**
     * This is a Many-to-One relationship between Invoice and Customer entities.
     * The 'customer' field in the Invoice entity is joined by the 'customer_id' column in the Customer entity.
     * The 'customer_id' column cannot be null, meaning every invoice must be associated with a customer.
     * The @JsonIgnore annotation is used to prevent circular references during serialization,
     * which means the customer field will not be included when an Invoice entity is converted to JSON.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

}
