package com.sass.business.models;

import jakarta.persistence.*;

import java.util.UUID;
@Entity
@Table(name = "business")
public class Business {
    //region ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private byte[] uuid;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "image_path", length = 250)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "uuid_user", referencedColumnName = "uuid")
    private User user;

    @Column(name = "description", length = 250)
    private String description;

    //endregion

    // region GETTERS AND SETTERS

    public byte[] getUuid() {
        return uuid;
    }

    public void setUuid(byte[] uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    //endregion
}
