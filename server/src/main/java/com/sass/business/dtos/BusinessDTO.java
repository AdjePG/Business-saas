package com.sass.business.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class BusinessDTO {

    @NotNull(message = "El UUID del negocio no puede ser nulo")
    //private UUID uuid;
    private Long uuid;

    @NotEmpty(message = "El nombre no puede ser nulo")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotNull(message = "El UUID del User no puede ser nulo")
    //private UUID uuidUser;
    private Long uuidUser;

    // Getters y Setters
    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUuidUser() {
        return uuidUser;
    }

    public void setUuidUser(Long uuidUser) {
        this.uuidUser = uuidUser;
    }
}
