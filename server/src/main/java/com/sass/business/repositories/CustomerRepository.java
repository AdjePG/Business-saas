package com.sass.business.repositories;

import com.sass.business.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository  extends JpaRepository<Customer, byte[]> {
}
