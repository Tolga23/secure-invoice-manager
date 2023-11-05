package com.thardal.secureinvoicemanager.user.repository;

import com.thardal.secureinvoicemanager.user.entity.ResetPasswordVerifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ResetPasswordVerificationsRepository extends JpaRepository<ResetPasswordVerifications, Long> {

    void deleteResetPasswordVerificationsByUserId(Long userId);

    @Modifying
    @Query(value = "INSERT INTO reset_password_verifications (user_id,expiration_date,url) " +
            "VALUES (:userId, :expirationDate, :url)", nativeQuery = true)
    void insertResetPasswordVerifications(@Param("userId") Long userId, @Param("expirationDate") String expirationDate, @Param("url") String url);

    @Query(value = "SELECT CASE WHEN expiration_date < NOW() THEN true ELSE false END AS is_expired FROM reset_password_verifications WHERE url = :url", nativeQuery = true)
    Long isUrlExpired(@Param("url") String url);


}
