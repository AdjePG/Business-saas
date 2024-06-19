package com.sass.business.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LogInDTO {
    // region ATTRIBUTES

    @Email
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Size(max = 255, message = "El correo electrónico no puede tener más de 255 caracteres")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(max = 255, message = "La contraseña no puede tener más de 255 caracteres")
    private String password;

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

    public void SetPassword(String password) {
        this.password = password;
    }

    //endregion
}

