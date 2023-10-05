package com.thardal.secureinvoicemanager.user.repository;

import com.thardal.secureinvoicemanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);

    User getUserById(Long userId);

    @Query("SELECT u FROM User u WHERE u.id = (SELECT v.userId FROM TwoFactorVerifications v WHERE v.verificationCode = :code)")
    User findUserByVerificationCode(String code);
}
