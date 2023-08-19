package com.thardal.secureinvoicemanager.user.repository;

import com.thardal.secureinvoicemanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
