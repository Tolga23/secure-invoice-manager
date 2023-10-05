package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.role.service.RoleService;
import com.thardal.secureinvoicemanager.user.converter.UserConverter;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.User;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
import com.thardal.secureinvoicemanager.user.service.entityservice.UserEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.thardal.secureinvoicemanager.base.utils.SmsUtils.sendSMS;
import static com.thardal.secureinvoicemanager.user.enums.RoleType.USER_ROLE;
import static com.thardal.secureinvoicemanager.user.enums.VerificationType.ACCOUNT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private static String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private UserEntityService userEntityService;
    private UserConverter userConverter;
    private RoleService roleService;
    private UserVerificationService userVerificationService;
    private PasswordEncoder passwordEncoder;
    private TwoFactorVerificationService twoFactorVerificationService;

    public UserService(UserEntityService userEntityService, UserConverter userConverter, RoleService roleService, UserVerificationService userVerificationService, PasswordEncoder passwordEncoder, TwoFactorVerificationService twoFactorVerificationService) {
        this.userEntityService = userEntityService;
        this.userConverter = userConverter;
        this.roleService = roleService;
        this.userVerificationService = userVerificationService;
        this.passwordEncoder = passwordEncoder;
        this.twoFactorVerificationService = twoFactorVerificationService;
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

    public UserDto findUserByVerificationCode(String code) {
        User user = userEntityService.findUserByVerificationCode(code);

        UserDto dto = userConverter.toDto(user);

        return dto;
    }

    public UserDto verifyCode(String email, String code) {

        try {
            UserDto userByCode = findUserByVerificationCode(code);
            UserDto userByEmail = getUserByEmail(email);

            if (userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())) {
                return userByCode;
            }

        } catch (Exception ex) {
            throw new ApiException("Code is invalid");
        }

        return null;

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
        UserDto user = findUserByEmail(email);
        validateUser(user);

        return createUserDetails(user);
    }

    private UserPrincipal createUserDetails(UserDto user) {
        String permission = roleService.getRoleByUserId(user.getId()).getPermission();
        return new UserPrincipal(user, permission);
    }

    private void validateUser(UserDto user) {
        if (!user.isEnable()) {
            log.error("USER DISABLED ERROR: {}", user.isEnable());
            throw new DisabledException("User is disabled");
        }
    }

    private UserDto findUserByEmail(String email) {
        UserDto user = getUserByEmail(email);

        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        log.info("User found in the database: {}", email);
        return user;
    }

    public UserDto getUserByEmail(String email) {
        User user = userEntityService.getUserByEmail(email);
        UserDto userDto = userConverter.toDto(user);
        return userDto;
    }

    public void sendVerificationCode(UserDto user) {
        String expirationDate = format(addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = randomAlphabetic(8).toUpperCase();

        try {
            twoFactorVerificationService.deleteByUserId(user.getId());
            twoFactorVerificationService.updateByUserIdAndVerificationCodeAndExpirationDate(user.getId(), verificationCode, expirationDate);
            sendSMS(user.getPhone(), "From: SecureInvoice \nVerification code\n" + verificationCode);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ApiException("An error occurred. Please try again");
        }

    }
}
