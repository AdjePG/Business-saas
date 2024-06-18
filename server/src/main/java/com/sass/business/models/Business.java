package com.sass.business.models;

import jakarta.persistence.*;

import java.util.UUID;
@Entity
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "name", length = 10)
    private String name;

    // Otros campos

    @ManyToOne
    @JoinColumn(name = "uuid_user", referencedColumnName = "uuid")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
