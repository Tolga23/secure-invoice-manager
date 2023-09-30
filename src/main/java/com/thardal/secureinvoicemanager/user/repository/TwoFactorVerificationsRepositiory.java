package com.thardal.secureinvoicemanager.user.repository;

import com.thardal.secureinvoicemanager.user.entity.TwoFactorVerifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface TwoFactorVerificationsRepositiory extends JpaRepository<TwoFactorVerifications,Long> {
    void deleteByUserId(Long userId);
    @Modifying
    @Query(value = "INSERT INTO two_factor_verifications (user_id, verification_code, expiration_date) " +
            "VALUES (:userId, :verificationCode, :expirationDate)", nativeQuery = true)
    void updateByUserIdAndVerificationCodeAndExpirationDate(@Param("userId") Long userId, @Param("verificationCode") String verificationCode, @Param("expirationDate") String expirationDate);
}
