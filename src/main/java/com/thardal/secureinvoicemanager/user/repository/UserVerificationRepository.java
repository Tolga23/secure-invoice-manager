package com.thardal.secureinvoicemanager.user.repository;

import com.thardal.secureinvoicemanager.user.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {

}
