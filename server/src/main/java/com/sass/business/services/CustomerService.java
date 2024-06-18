package com.sass.business.services;

import com.sass.business.dtos.customer.CreateCustomerDTO;
import com.sass.business.dtos.customer.CustomerDTO;
import com.sass.business.mappers.CustomerMapper;
import com.sass.business.models.Business;
import com.sass.business.models.Customer;
import com.sass.business.repositories.BusinessRepository;
import com.sass.business.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, BusinessRepository businessRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.businessRepository = businessRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customer = customerRepository.findAll();

        return customer.stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(UUID uuid) {

        return customerRepository.findById(uuid)
                .map(customerMapper::toDto)
                .orElse(null);
    }

    public CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        Business business = businessRepository.findById(createCustomerDTO.getUuidBusiness())
                .orElseThrow(() -> new IllegalArgumentException("Invalid business UUID"));
        Customer customer = customerMapper.toModel(createCustomerDTO, business);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDTO updateCustomer(UUID uuid, CustomerDTO customerDTO) {
        return customerRepository.findById(uuid).map(existingCustomer -> {
            Business business = businessRepository.findById(customerDTO.getUuidBusiness())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid business UUID"));
            Customer customer = customerMapper.toModel(customerDTO, business);
            customer.setUuid(existingCustomer.getUuid()); // Preserva el UUID existente
            Customer updatedCustomer = customerRepository.save(customer);
            return customerMapper.toDto(updatedCustomer);
        }).orElse(null);
    }

    public void deleteCustomer(UUID uuid) {
        customerRepository.deleteById(uuid);
    }
}



