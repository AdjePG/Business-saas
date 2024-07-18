package com.sass.business.repositories;

import com.sass.business.models.business.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessRepository  extends JpaRepository<Business, byte[]> {
    List<Business> findByUserUuid(byte[] userId);
}
