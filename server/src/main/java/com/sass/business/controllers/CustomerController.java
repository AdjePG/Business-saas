package com.sass.business.controllers;


import com.sass.business.dtos.customer.CreateCustomerDTO;
import com.sass.business.dtos.customer.CustomerDTO;
import com.sass.business.mappers.CustomerMapper;
import com.sass.business.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
//@Api(value = "Sistema de Gestió de Clients", description = "Operacions relatives als clients en el Sistema de Gestió de Clients")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    //@ApiOperation(value = "Veure una llista de clients disponibles", response = List.class)
    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    //@ApiOperation(value = "Obtenir un client per Id")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            //@ApiParam(value = "Id del client del qual es recuperarà l'objecte client", required = true)
            @PathVariable UUID id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        if (customerDTO != null) {
            return ResponseEntity.ok(customerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //@ApiOperation(value = "Afegir un client")
    @PostMapping
    public CustomerDTO createCustomer(
            //@ApiParam(value = "Objecte client que s'emmagatzemarà a la taula de la base de dades", required = true)
            @Valid @RequestBody CreateCustomerDTO createCustomerDTO) {
        return customerService.createCustomer(createCustomerDTO);
    }

    //@ApiOperation(value = "Actualitzar un client")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            //@ApiParam(value = "Id del client per actualitzar l'objecte client", required = true)
            @PathVariable UUID id,
            //@ApiParam(value = "Actualitzar objecte client", required = true)
            @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(id, customerDTO);
        if (updatedCustomerDTO != null) {
            return ResponseEntity.ok(updatedCustomerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //@ApiOperation(value = "Eliminar un client")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            //@ApiParam(value = "Id del client del qual s'eliminarà l'objecte client de la taula de la base de dades", required = true)
            @PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
