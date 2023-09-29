package com.thardal.secureinvoicemanager.role.service;

import com.thardal.secureinvoicemanager.role.converter.RoleConverter;
import com.thardal.secureinvoicemanager.role.dto.RoleDto;
import com.thardal.secureinvoicemanager.role.entity.Role;
import com.thardal.secureinvoicemanager.role.service.entityservice.RoleEntityService;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleEntityService roleEntityService;
    private final UserRolesService userRolesService;
    private final RoleConverter roleConverter;

    public void addRoleToUser(Long userId, String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);

        userRolesService.addUserRole(userId, role.getId());

        roleEntityService.save(role);
    }

    public RoleDto getRoleByUserId(Long userId){

        try {
            Role role = roleEntityService.getRoleByUserId(userId);

            RoleDto roleDto = roleConverter.toDto(role);
            log.info("Roles found: " + roleDto.toString());
            return roleDto;

        }catch (EmptyResultDataAccessException ex) {
            throw new ApiException("No role found by name: ");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }

    }

}
