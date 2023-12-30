package com.thardal.secureinvoicemanager.role.service;

import com.thardal.secureinvoicemanager.role.entity.UserRoles;
import com.thardal.secureinvoicemanager.role.service.entityservice.UserRolesEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRolesService {
    private final UserRolesEntityService userRolesEntityService;

    public void addUserRole(Long userId, Long roleId) {
        UserRoles userRoles = createUserRoles(userId, roleId);
        userRolesEntityService.save(userRoles);
    }

    private UserRoles createUserRoles(Long userId, Long roleId) {
        UserRoles userRoles = new UserRoles();
        userRoles.setRoleId(roleId);
        userRoles.setUserId(userId);
        return userRoles;
    }

    public void updateUserRole(Long userId, Long roleId){
        if (getUserRoleByUserId(userId).isPresent()){
            userRolesEntityService.updateUserRole(userId, roleId);
        } else {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

    }

    public Optional<UserRoles> getUserRoleByUserId(Long userId) {
        return userRolesEntityService.getUserRolesByUserId(userId);
    }
}
