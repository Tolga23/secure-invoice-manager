package com.thardal.secureinvoicemanager.customer.controller;

import com.thardal.secureinvoicemanager.base.entity.HttpResponse;
import com.thardal.secureinvoicemanager.customer.entity.Customer;
import com.thardal.secureinvoicemanager.customer.entity.Invoice;
import com.thardal.secureinvoicemanager.customer.service.CustomerService;
import com.thardal.secureinvoicemanager.report.CustomerReport;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getCustomers(@AuthenticationPrincipal UserDto user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(HttpResponse.of(OK, "Customers retrieved successfully", Map.of(
                "user", userService.getUserById(user.getId()),
                "customers", customerService.getCustomers(page.orElse(0), size.orElse(10)),
                "stats", customerService.getStats())));
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCustomer(@AuthenticationPrincipal UserDto user, @RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.of(CREATED, "Customer created successfully", Map.of(
                        "user", userService.getUserById(user.getId()),
                        "customers", customerService.createCustomer(customer))));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCustomer(@AuthenticationPrincipal UserDto user, @PathVariable("id") Long customerId) {
        return ResponseEntity.ok(HttpResponse.of(OK, "Customer retrieved successfully", Map.of(
                "user", userService.getUserById(user.getId()),
                "customer", customerService.getCustomerById(customerId))));
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchCustomer(@AuthenticationPrincipal UserDto user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(HttpResponse.of(OK, "Customer retrieved successfully", Map.of(
                "user", userService.getUserById(user.getId()),
                "customers", customerService.searchCustomer(name.orElse(""), page.orElse(0), size.orElse(10)))));
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCustomer(@AuthenticationPrincipal UserDto user, @RequestBody Customer customer) {
        return ResponseEntity.ok(HttpResponse.of(OK, "Customer updated successfully", Map.of(
                "user", userService.getUserById(user.getId()),
                "customer", customerService.updateCustomer(customer))));
    }

    @PostMapping("/invoce/create")
    public ResponseEntity<HttpResponse> createInvoice(@AuthenticationPrincipal UserDto user, @RequestBody Invoice invoice) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.of(CREATED, "Invoice created successfully", Map.of(
                        "user", userService.getUserById(user.getId()),
                        "invoice", customerService.createInvoice(invoice))));
    }

    @GetMapping("/invoice/new")
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal UserDto user) {
        return ResponseEntity.ok(HttpResponse.of(OK,"", Map.of(
                "user", userService.getUserById(user.getId()),
                "customer", customerService.getCustomers())));
    }

    @GetMapping("/invoice/list")
    public ResponseEntity<HttpResponse> getInvoices(@AuthenticationPrincipal UserDto user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(HttpResponse.of(OK, "Invoices retrieved successfully", Map.of(
                "user", userService.getUserById(user.getId()),
                "page", customerService.getInvoices(page.orElse(0), size.orElse(10)))));
    }

    @GetMapping("/invoice/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal UserDto user, @PathVariable("id") Long invoiceId) {
        Invoice invoice = customerService.getInvoiceById(invoiceId);
        return ResponseEntity.ok(HttpResponse.of(OK, "Invoice retrieved successfully", Map.of(
                "user", userService.getUserById(user.getId()),
                "invoice", invoice,
                "customer", invoice.getCustomer())));
    }

    @PostMapping("/invoice/add/{id}")
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(@AuthenticationPrincipal UserDto user, @PathVariable("id") Long customerId, @RequestBody Invoice invoice) {
        customerService.addInvoiceToCustomer(customerId, invoice);
        return ResponseEntity.ok(HttpResponse.of(OK, "Invoice added to customer with ID: "+ customerId, Map.of(
                "user", userService.getUserById(user.getId()),
                "customer", customerService.getCustomers())));
    }

    @GetMapping("/download/report")
    public ResponseEntity<Resource> downloadReport() {
        List<Customer> customers = new ArrayList<>();
        customerService.getCustomers().forEach(customers::add);
        CustomerReport report = new CustomerReport(customers);
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name","customers-report.xlsx");
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=customers-report.xlsx");
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .headers(headers).body(report.export());
    }
}
