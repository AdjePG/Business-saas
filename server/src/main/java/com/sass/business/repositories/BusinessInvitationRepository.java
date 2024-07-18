package com.sass.business.repositories;

import com.sass.business.models.business.BusinessInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessInvitationRepository extends JpaRepository<BusinessInvitation, byte[]> {
}
