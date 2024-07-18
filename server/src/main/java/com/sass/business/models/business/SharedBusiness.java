package com.sass.business.models.business;

import com.sass.business.models.User;
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

    @Column(name = "permissions")
    private String permissions;

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

    // endregion
}
