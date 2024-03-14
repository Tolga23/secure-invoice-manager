package com.thardal.secureinvoicemanager.customer.service;

import com.thardal.secureinvoicemanager.customer.entity.Customer;
import com.thardal.secureinvoicemanager.customer.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    Page<Customer> getCustomers(int page, int size);

    Iterable<Customer> getCustomers();

    Customer getCustomerById(Long id);

    Page<Customer> searchCustomer(String name, int page, int size);

    Invoice createInvoice(Invoice invoice);

    Page<Invoice> getInvoices(int page, int size);

    void addInvoiceToCustomer(Long customerId, Invoice invoice);

    Invoice getInvoiceById(Long invoiceId);
}
