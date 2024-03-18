package com.thardal.secureinvoicemanager.customer.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

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
