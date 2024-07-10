package com.sass.business.models;

import com.sass.business.models.pks.UserBusinessPK;
import jakarta.persistence.*;

@Entity
@Table(name = "shared_business")
@IdClass(UserBusinessPK.class)
public class SharedBusiness {
    // region ATTRIBUTES

    @Id
    @ManyToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "business_uuid", referencedColumnName = "uuid", nullable = false)
    private Business business;

    private String permissions;

    private boolean accepted;

    // endregion

    // region GETTERS AND SETTERS

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    // endregion
}
