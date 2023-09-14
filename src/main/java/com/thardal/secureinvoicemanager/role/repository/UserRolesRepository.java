package com.thardal.secureinvoicemanager.role.repository;

import com.thardal.secureinvoicemanager.role.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles,Long> {
}
