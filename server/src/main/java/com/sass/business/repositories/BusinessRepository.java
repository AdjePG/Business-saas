package com.sass.business.repositories;

import com.sass.business.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessRepository  extends JpaRepository<Business, UUID> {
}
