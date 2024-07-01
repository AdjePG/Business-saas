package com.sass.business.dtos.business;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class BusinessDTO {
    // region ATTRIBUTES

    @NotNull(message = "El UUID del negocio no puede ser nulo")
    private UUID uuid;

    @NotEmpty(message = "El nombre no puede ser nulo")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotNull(message = "El UUID del User no puede ser nulo")
    private UUID uuidUser;

    private String imagePath;

    private String description;

    // endregion

    // region GETTERS AND SETTERS

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuidUser() {
        return uuidUser;
    }

    public void setUuidUser(UUID uuidUser) {
        this.uuidUser = uuidUser;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // endregion
}
