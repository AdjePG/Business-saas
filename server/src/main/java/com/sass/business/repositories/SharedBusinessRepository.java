package com.sass.business.repositories;

import com.sass.business.models.business.SharedBusiness;
import com.sass.business.models.pks.UserBusinessPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedBusinessRepository extends JpaRepository<SharedBusiness, UserBusinessPK> {
    List<SharedBusiness> findByUserUuid(byte[] userId);
}
