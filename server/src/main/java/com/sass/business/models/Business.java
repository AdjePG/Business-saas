package com.sass.business.models;

import jakarta.persistence.*;

import java.util.UUID;
@Entity
@Table(name = "business")
public class Business {
    //region ATTRIBUTES

    @Id
    //@GeneratedValue
    //@Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usa IDENTITY para delegar la generación del ID al motor de la base de datos
    //private UUID uuid;
    private Long uuid;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "imagePath", length = 250)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "uuid_user", referencedColumnName = "uuid")
    private User user;
    //endregion

    // region GETTERS AND SETTERS

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
    //endregion

}
