package com.sass.business.models.business;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "business_invitation")
public class BusinessInvitation {
    // region ATTRIBUTES

    @Id
    @Column(name = "token", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private byte[] token;

    @ManyToOne
    @JoinColumn(name = "business_uuid", referencedColumnName = "uuid", nullable = false)
    private Business business;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime expiresAt;

    // endregion

    // region GETTERS AND SETTERS

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    // endregion

    // region OTHERS

    @PrePersist
    protected void onCreate() {
        this.expiresAt = LocalDateTime.now().plusHours(1);
    }

    // endregion
}
