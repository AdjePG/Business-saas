package com.sass.business.dtos.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    // region ATTRIBUTES

    @Email
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Size(max = 255, message = "El correo electrónico no puede tener más de 255 caracteres")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(max = 255, message = "La contraseña no puede tener más de 255 caracteres")
    private String password;

    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @Size(max = 50, message = "El primer apellido no puede tener más de 50 caracteres")
    private String firstSurname;

    @Size(max = 50, message = "El segundo apellido no puede tener más de 50 caracteres")
    private String lastSurname;

    @Size(max = 255, message = "La foto no puede tener más de 255 caracteres")
    private String photo;

    @Size(max = 2, message = "El país no puede tener más de 255 caracteres")
    private String country;

    @Size(max = 255, message = "La ciudad no puede tener más de 255 caracteres")
    private String location;

    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String address;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String phone;

    @Size(max = 255, message = "El correo electrónico de contacto no puede tener más de 255 caracteres")
    private String contactEmail;

    // endregion

    //region GETTERS AND SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getLastSurname() {
        return lastSurname;
    }

    public void setLastSurname(String lastSurname) {
        this.lastSurname = lastSurname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    //endregion
}
