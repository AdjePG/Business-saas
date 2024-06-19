package com.sass.business.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UserDTO {
    // region ATTRIBUTES

    //private UUID uuid;
    private Long uuid;

    @Email
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Size(max = 255, message = "El correo electrónico no puede tener más de 255 caracteres")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(max = 255, message = "La contraseña no puede tener más de 255 caracteres")
    private String password;

    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @Size(max = 255, message = "La foto no puede tener más de 255 caracteres")
    private String photo;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String phone;

    // endregion

    //region GETTERS AND SETTERS

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //endregion
}

