package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.role.service.RoleService;
import com.thardal.secureinvoicemanager.user.converter.UserConverter;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.User;
import com.thardal.secureinvoicemanager.user.enums.RoleType;
import com.thardal.secureinvoicemanager.user.enums.VerificationType;
import com.thardal.secureinvoicemanager.user.service.entityservice.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserEntityService userEntityService;
    private final UserConverter userConverter;
    private final RoleService roleService;
    private final UserVerificationService userVerificationService;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAll() {
        List<User> userList = userEntityService.findAll();

        List<UserDto> userDtoList = userConverter.toDtoList(userList);

        return userDtoList;
    }

    public UserDto save(UserSaveRequestDto userSaveRequestDto) {

        User user = userConverter.convertToUserSave(userSaveRequestDto);

        String encodePassword = encodedUserPassword(user);
        user.setPassword(encodePassword);
        try {
            user.setNotLocked(true);

            user = userEntityService.save(user);

            String verificationUrl = getVerificationUrl(getRandomUUID(), VerificationType.ACCOUNT.getType());
            userVerificationService.createUserVerification(user.getId(),verificationUrl);
            roleService.addRoleToUser(user.getId(), RoleType.USER_ROLE.toString());

        } catch (DataIntegrityViolationException ex) {
            log.error(ex.getMessage());
            throw new DataIntegrityViolationException("Email already in use. Please use a different email adress");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException("Something went wrong. Please try again later");
        }

        UserDto userDto = userConverter.toDto(user);

        return userDto;
    }

    private String getVerificationUrl(String key, String verificationType) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + verificationType + "/" + key).toUriString();
    }

    private static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private String encodedUserPassword(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        return encodePassword;
    }

}
