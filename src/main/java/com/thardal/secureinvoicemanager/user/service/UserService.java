package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.role.service.RoleService;
import com.thardal.secureinvoicemanager.user.converter.UserConverter;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.User;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import com.thardal.secureinvoicemanager.user.service.entityservice.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.UUID;

import static com.thardal.secureinvoicemanager.user.enums.RoleType.USER_ROLE;
import static com.thardal.secureinvoicemanager.user.enums.VerificationType.ACCOUNT;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private UserEntityService userEntityService;
    private UserConverter userConverter;
    private RoleService roleService;
    private UserVerificationService userVerificationService;
    private PasswordEncoder passwordEncoder;

    public UserService(UserEntityService userEntityService, UserConverter userConverter, RoleService roleService, UserVerificationService userVerificationService, PasswordEncoder passwordEncoder) {
        this.userEntityService = userEntityService;
        this.userConverter = userConverter;
        this.roleService = roleService;
        this.userVerificationService = userVerificationService;
        this.passwordEncoder = passwordEncoder;
    }

    private static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

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

            userVerification(user);
            roleService.addRoleToUser(user.getId(), USER_ROLE.toString());

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

    private void userVerification(User user) {
        String verificationUrl = getVerificationUrl(getRandomUUID(), ACCOUNT.getType());
        userVerificationService.createUserVerification(user.getId(), verificationUrl);
    }

    private String getVerificationUrl(String key, String verificationType) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + verificationType + "/" + key).toUriString();
    }

    private String encodedUserPassword(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        return encodePassword;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByEmail(email);
        validateUser(user);

        return createUserDetails(user);
    }

    private UserPrincipal createUserDetails(User user) {
        String permission = roleService.getRoleByUserId(user.getId()).getPermission();
        return new UserPrincipal(user, permission);
    }

    private static void validateUser(User user) {
        if (!user.isEnable()) {
            log.error("USER DISABLED ERROR: {}", user.isEnable());
            throw new DisabledException("User is disabled");
        }
    }

    private User findUserByEmail(String email) {
        User user = getUserByEmail(email);

        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        log.info("User found in the database: {}", email);
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userEntityService.getUserByEmail(email);
        return user;
    }
}
