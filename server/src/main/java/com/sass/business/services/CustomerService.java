package com.sass.business.services;

import com.sass.business.dtos.customer.CreateCustomerDTO;
import com.sass.business.dtos.customer.CustomerDTO;
import com.sass.business.mappers.CustomerMapper;
import com.sass.business.models.business.Business;
import com.sass.business.models.Customer;
import com.sass.business.others.UuidConverterUtil;
import com.sass.business.repositories.BusinessRepository;
import com.sass.business.repositories.CustomerRepository;
import com.sass.exception.common.CustomServiceException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    // region INJECTED DEPENDENCIES

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final CustomerMapper customerMapper;
    private final UuidConverterUtil uuidConverterUtil;

    public CustomerService(
            CustomerRepository customerRepository,
            BusinessRepository businessRepository,
            CustomerMapper customerMapper,
            UuidConverterUtil uuidConverterUtil
    ) {
        this.customerRepository = customerRepository;
        this.businessRepository = businessRepository;
        this.customerMapper = customerMapper;
        this.uuidConverterUtil = uuidConverterUtil;
    }

    // endregion

    // region SERVICE METHODS

    public List<CustomerDTO> getCustomers() {
        List<Customer> customer = customerRepository.findAll();

        return customer.stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    /*public CustomerDTO getCustomerById(Long uuid) {

        return customerRepository.findById(uuid)
                .map(customerMapper::toDto)
                .orElse(null);
    }*/

    public CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        try {
            Business business = businessRepository.findById(uuidConverterUtil.uuidToBytes(createCustomerDTO.getUuidBusiness()))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid business UUID: " + createCustomerDTO.getUuidBusiness()));
            Customer customer = customerMapper.toModel(createCustomerDTO, business);
            Customer savedCustomer = customerRepository.save(customer);
            return customerMapper.toDto(savedCustomer);
        } catch (Exception e) {
            logger.error("Error creating customer: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to create customer: " + e.getMessage(), e);
        }
    }

    public CustomerDTO updateCustomer(UUID uuid, CustomerDTO customerDTO) {
        return customerRepository.findById(uuidConverterUtil.uuidToBytes(uuid)).map(existingCustomer -> {
            Business business = businessRepository.findById(uuidConverterUtil.uuidToBytes(customerDTO.getUuidBusiness()))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid business UUID: " + customerDTO.getUuidBusiness()));
            Customer customer = customerMapper.toModel(customerDTO, business);
            customer.setUuid(existingCustomer.getUuid()); // Preserva el UUID existente
            Customer updatedCustomer = customerRepository.save(customer);
            return customerMapper.toDto(updatedCustomer);
        }).orElse(null);
    }

    public void deleteCustomer(UUID uuid) {
        customerRepository.deleteById(uuidConverterUtil.uuidToBytes(uuid));
    }

    // endregion
}



