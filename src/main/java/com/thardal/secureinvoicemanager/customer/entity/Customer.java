package com.thardal.secureinvoicemanager.customer.entity;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String type;
    private String status;
    private String imageUrl;
    /**
     * This is a One-to-Many relationship between Customer and Invoice entities.
     * It is mapped by the 'customer' field in the Invoice entity.
     * FetchType is LAZY which means the invoices are fetched only when they are accessed for the first time.
     * CascadeType is ALL, so all operations (persist, merge, refresh, remove, detach) that happen on Customer will also cascade to the invoices.
     */
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Invoice> invoices;

}
