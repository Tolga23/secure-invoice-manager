package com.thardal.secureinvoicemanager.role.repository;

import com.thardal.secureinvoicemanager.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Transactional
    @Modifying
    @Query("update Role r set r.roleName = :roleName where r.id = :id")
    void updateRoleByIdAndRoleName(Long id, String roleName);

    @Query("SELECT r FROM Role r " +
            "JOIN UserRoles ur ON ur.roleId = r.id " +
            "JOIN User u ON u.id = ur.userId " +
            "WHERE u.id = :userId")
    Role getRoleByUserId(@Param("userId") Long userId);


//    @Modifying
//    @Query("UPDATE Role r SET r.roleName = :roleName WHERE r.userId = :userId")
//    void updateRoleByUserIdAndRoleName(Long userId, String roleName);

    Role findRoleByRoleName(String roleName);
}
