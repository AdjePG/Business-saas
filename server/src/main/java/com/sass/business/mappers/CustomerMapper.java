package com.sass.business.mappers;

import com.sass.business.dtos.customer.CreateCustomerDTO;
import com.sass.business.dtos.customer.CustomerDTO;
import com.sass.business.models.business.Business;
import com.sass.business.models.Customer;
import com.sass.business.others.UuidConverterUtil;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    // region INJECTED DEPENDENCIES

    private UuidConverterUtil uuidConverterUtil;

    public CustomerMapper(
            UuidConverterUtil uuidConverterUtil
    ) {
        this.uuidConverterUtil = uuidConverterUtil;
    }

    // endregion

    // Convierte de entidad a DTO
    public CustomerDTO toDto(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUuid(uuidConverterUtil.binaryToUuid(customer.getUuid()));
        dto.setUuidBusiness(uuidConverterUtil.binaryToUuid(customer.getBusiness().getUuid()));
        dto.setName(customer.getName());
        dto.setSurname(customer.getSurname());
        dto.setTaxName(customer.getTaxName());
        dto.setTaxNumber(customer.getTaxNumber());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setType(customer.getType());
        dto.setLocation(customer.getLocation());
        dto.setAddress(customer.getAddress());
        return dto;
    }

    // Convierte de CreateCustomerDTO a entidad
    public Customer toModel(CreateCustomerDTO dto, Business business) {
        Customer customer = new Customer();
        customer.setBusiness(business);
        customer.setName(dto.getName());
        customer.setSurname(dto.getSurname());
        customer.setTaxName(dto.getTaxName());
        customer.setTaxNumber(dto.getTaxNumber());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setType(dto.getType());
        customer.setLocation(dto.getLocation());
        customer.setAddress(dto.getAddress());
        return customer;
    }

    // Convierte de CustomerDTO a entidad
    public Customer toModel(CustomerDTO dto, Business business) {
        Customer customer = new Customer();
        customer.setUuid(uuidConverterUtil.uuidToBytes(dto.getUuid()));
        customer.setBusiness(business);
        customer.setName(dto.getName());
        customer.setSurname(dto.getSurname());
        customer.setTaxName(dto.getTaxName());
        customer.setTaxNumber(dto.getTaxNumber());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setType(dto.getType());
        customer.setLocation(dto.getLocation());
        customer.setAddress(dto.getAddress());
        return customer;
    }
}
