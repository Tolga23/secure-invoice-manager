package com.thardal.secureinvoicemanager.customer.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    private Long totalCustomers;
    private Long totalInvoices;
    private Long totalInvoicesPaid;

}
