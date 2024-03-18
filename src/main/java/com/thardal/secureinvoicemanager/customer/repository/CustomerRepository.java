package com.thardal.secureinvoicemanager.customer.repository;

import com.thardal.secureinvoicemanager.customer.entity.Customer;
import com.thardal.secureinvoicemanager.customer.entity.Stats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, ListCrudRepository<Customer, Long> {
    Page<Customer> findByNameContaining(String name, Pageable pageable);

    @Query("select new com.thardal.secureinvoicemanager.customer.entity.Stats(c.totalCustomers, i.totalInvoices, tp.totalInvoicesPaid) " +
            "from (select count(*) as totalCustomers from Customer) c, " +
            "(select count(*) as totalInvoices from Invoice) i, " +
            "(select sum(total) as totalInvoicesPaid from Invoice) tp")
    Stats getStats();

}
