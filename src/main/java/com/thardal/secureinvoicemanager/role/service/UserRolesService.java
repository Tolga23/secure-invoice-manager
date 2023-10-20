package com.thardal.secureinvoicemanager.role.service;

import com.thardal.secureinvoicemanager.role.entity.UserRoles;
import com.thardal.secureinvoicemanager.role.service.entityservice.UserRolesEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRolesService {
    private final UserRolesEntityService userRolesEntityService;

    public void addUserRole(Long userId, Long roleId) {
        UserRoles userRoles = new UserRoles();
        userRoles.setRoleId(roleId);
        userRoles.setUserId(userId);

        userRolesEntityService.save(userRoles);
    }
}
