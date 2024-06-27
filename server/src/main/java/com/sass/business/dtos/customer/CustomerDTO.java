package com.sass.business.dtos.customer;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CustomerDTO {
    // region ATTRIBUTES

    private Long uuid;

    @NotNull(message = "El UUID del negocio no puede ser nulo")
    private Long uuidBusiness;

    @NotEmpty(message = "El nombre no puede ser nulo")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotEmpty(message = "El apellido no puede ser nulo")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String surname;

    @Size(max = 50, message = "El nombre fiscal no puede tener más de 50 caracteres")
    private String taxName;

    @NotEmpty(message = "El número fiscal no puede ser nulo")
    private Integer taxNumber;

    @NotEmpty(message = "El correo electrónico no puede ser nulo")
    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 255, message = "El correo electrónico no puede tener más de 255 caracteres")
    private String email;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String phone;

    @NotEmpty(message = "El tipo no puede ser nulo")
    private Integer type;

    @Size(max = 200, message = "La ubicación no puede tener más de 200 caracteres")
    private String location;

    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    private String address;
    // endregion

    // region Getters and Setters
    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getUuidBusiness() {
        return uuidBusiness;
    }

    public void setUuidBusiness(Long uuidBusiness) {
        this.uuidBusiness = uuidBusiness;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Integer getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(Integer taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // endregion
}