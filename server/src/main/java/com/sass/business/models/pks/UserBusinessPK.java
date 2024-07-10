package com.sass.business.models.pks;

import com.sass.business.models.Business;
import com.sass.business.models.User;

import java.io.Serializable;
import java.util.Objects;

public class UserBusinessPK implements Serializable {
    private User user;
    private Business business;

    public UserBusinessPK(User user, Business business) {
        this.user = user;
        this.business = business;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBusinessPK that = (UserBusinessPK) o;
        return Objects.equals(user, that.user) && Objects.equals(business, that.business);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, business);
    }
}
