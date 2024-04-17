package com.thardal.secureinvoicemanager.customer.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    private Long totalCustomers;
    private Long totalInvoices;
    private Double totalInvoicesPaid;

}
