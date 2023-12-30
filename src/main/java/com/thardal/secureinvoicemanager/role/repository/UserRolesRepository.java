package com.thardal.secureinvoicemanager.role.repository;

import com.thardal.secureinvoicemanager.role.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE UserRoles u SET u.roleId = :roleId WHERE u.userId = :userId")
    void updateUserRole(Long userId, Long roleId);

    Optional<UserRoles> getUserRolesByUserId(Long userId);
}
