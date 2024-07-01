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
    // region INJECTED DEPENDENCIES

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerController(
            CustomerService customerService,
            CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    // endregion

    // region REQUEST METHODS

    // region GET - GETCUSTOMERS
    //@ApiOperation(value = "Veure una llista de clients disponibles", response = List.class)
    @GetMapping("/")
    public List<CustomerDTO> getCustomers() {
        return customerService.getCustomers();
    }
    // endregion

    /*
    // region GET - GETCUSTOMERBYID
    //@ApiOperation(value = "Obtenir un client per Id")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            //@ApiParam(value = "Id del client del qual es recuperarà l'objecte client", required = true)
            @PathVariable Long id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        if (customerDTO != null) {
            return ResponseEntity.ok(customerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // endregion
    */

    // region POST - CREATECUSTOMER
    //@ApiOperation(value = "Afegir un client")
    @PostMapping("/")
    public CustomerDTO createCustomer(
            //@ApiParam(value = "Objecte client que s'emmagatzemarà a la taula de la base de dades", required = true)
            @Valid @RequestBody CreateCustomerDTO createCustomerDTO) {
        return customerService.createCustomer(createCustomerDTO);
    }
    // endregion

    // region PUT - UPDATECUSTOMER
    //@ApiOperation(value = "Actualitzar un client")
    @PutMapping("/{uuid}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            //@ApiParam(value = "Id del client per actualitzar l'objecte client", required = true)
            @PathVariable UUID uuid,
            //@ApiParam(value = "Actualitzar objecte client", required = true)
            @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(uuid, customerDTO);
        if (updatedCustomerDTO != null) {
            return ResponseEntity.ok(updatedCustomerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // endregion

    // region DELETE - DELETECUSTOMER
    //@ApiOperation(value = "Eliminar un client")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCustomer(
            //@ApiParam(value = "Id del client del qual s'eliminarà l'objecte client de la taula de la base de dades", required = true)
            @PathVariable UUID uuid) {
        customerService.deleteCustomer(uuid);
        return ResponseEntity.noContent().build();
    }
    // endregion

    // endregion
}
