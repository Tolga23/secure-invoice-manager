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

    @Query("SELECT u from User u where u.id = (SELECT r.userId from ResetPasswordVerifications r where r.url = :url)")
    User findUserByResetPasswordVerification(String url);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password  where u.id = (SELECT r.userId from ResetPasswordVerifications r where r.url = :url)")
    void updatePasswordByUrl(String password, String url);

    @Query("Select u FROM User u where u.id = (select v.userId from UserVerification v where v.url = :url)")
    User findUserByVerificationUrl(String url);

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.enable = true where u.id = :userId")
    void updateUserEnabledById(Long userId);

    @Modifying
    @Transactional
    @Query("Update User u set u.password = :password where u.id = :userId")
    void updatePassword(Long userId, String password);

    @Modifying
    @Transactional
    @Query("Update User u set u.enable = :enable, u.isNotLocked = :isNotLocked where u.id = :userId")
    void updateAccountSettings(Long userId, Boolean enable, Boolean isNotLocked);

    @Modifying
    @Transactional
    @Query("Update User u set u.isUsingAuth = :isUsingAuth where u.email = :email")
    void updateIsUsingAuthByEmail(String email, Boolean isUsingAuth);
}
