package com.thardal.secureinvoicemanager.role.repository;

import com.thardal.secureinvoicemanager.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Transactional
    @Modifying
    @Query("update Role r set r.roleName = :roleName where r.id = :id")
    void updateRoleByIdAndRoleName(Long id,String roleName);

    @Modifying
    Role getRoleByUserId(Long userId);

    @Modifying
    @Query("UPDATE Role r SET r.roleName = :roleName WHERE r.userId = :userId")
    void updateRoleByUserIdAndRoleName(Long userId, String roleName);

    Role findByRoleName(String roleName);

}
