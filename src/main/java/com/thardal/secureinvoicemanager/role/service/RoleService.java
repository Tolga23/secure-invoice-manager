package com.thardal.secureinvoicemanager.role.service;

import com.thardal.secureinvoicemanager.role.entity.Role;
import com.thardal.secureinvoicemanager.role.service.entityservice.RoleEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleEntityService roleEntityService;
    private final UserRolesService userRolesService;

    public void addRoleToUser(Long userId, String roleName) {
        Role role = new Role();
        role.setUserId(userId);
        role.setRoleName(roleName);

        userRolesService.addUserRole(userId, role.getId());

        roleEntityService.save(role);
    }

    public void updateRoleByUserIdAndRoleName(Long userId, String roleName) {
        Role role = new Role();
        role.setUserId(userId);
        role.setRoleName(roleName);


        roleEntityService.update(role);
    }

}
