package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.base.enums.BaseErrorMessages;
import com.thardal.secureinvoicemanager.base.enums.GlobalErrorMessages;
import com.thardal.secureinvoicemanager.base.exceptions.BusinessException;
import com.thardal.secureinvoicemanager.role.service.RoleService;
import com.thardal.secureinvoicemanager.user.converter.UserConverter;
import com.thardal.secureinvoicemanager.user.dto.ProfileUpdateDto;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.User;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import com.thardal.secureinvoicemanager.user.enums.UserErrorMessages;
import com.thardal.secureinvoicemanager.user.enums.VerificationType;
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
import java.util.Optional;
import java.util.UUID;

import static com.thardal.secureinvoicemanager.base.utils.SmsUtils.sendSMS;
import static com.thardal.secureinvoicemanager.user.enums.RoleType.USER_ROLE;
import static com.thardal.secureinvoicemanager.user.enums.VerificationType.ACCOUNT;
import static com.thardal.secureinvoicemanager.user.enums.VerificationType.PASSWORD;
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

    private ResetPasswordVerificationsService resetPasswordVerificationsService;

    public UserService(UserEntityService userEntityService, UserConverter userConverter, RoleService roleService, UserVerificationService userVerificationService,
                       PasswordEncoder passwordEncoder, TwoFactorVerificationService twoFactorVerificationService, ResetPasswordVerificationsService resetPasswordVerificationsService) {
        this.userEntityService = userEntityService;
        this.userConverter = userConverter;
        this.roleService = roleService;
        this.userVerificationService = userVerificationService;
        this.passwordEncoder = passwordEncoder;
        this.twoFactorVerificationService = twoFactorVerificationService;
        this.resetPasswordVerificationsService = resetPasswordVerificationsService;
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
            throw new DataIntegrityViolationException(UserErrorMessages.EMAIL_ALREADY_IN_USE.getMessage());
        } catch (BusinessException ex) {
            log.error(ex.getMessage());
            throw new BusinessException(GlobalErrorMessages.ERROR_OCCURRED);
        }

        UserDto userDto = userConverter.toDto(user);

        return userDto;
    }

    public UserDto verifyCode(String email, String code) {
        if (isVerificationCodeExpired(code) != 0) throw new ApiException(UserErrorMessages.CODE_EXPIRED);

        try {
            UserDto userByCode = findUserByVerificationCode(code);
            UserDto userByEmail = getUserByEmail(email);

            if (userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())) {
                twoFactorVerificationService.deleteByVerificationCode(code);
                return userByCode;
            } else {
                throw new ApiException(UserErrorMessages.INCORRECT_EMAIL);
            }

        } catch (Exception ex) {
            throw new ApiException(UserErrorMessages.INVALID_CODE);
        }

    }

    public void resetPassword(String email) {
        UserDto userByEmail = getUserByEmail(email);

        try {
            String expirationDate = getExpirationDate();
            String verificationUrl = getVerificationUrl(getRandomUUID(), PASSWORD.getType());

            resetPasswordVerificationsService.deleteResetPasswordVerificationsByUserId(userByEmail.getId());
            resetPasswordVerificationsService.insertResetPasswordVerifications(userByEmail.getId(), expirationDate, verificationUrl);
            log.info(verificationUrl);
        } catch (Exception ex) {
            throw new BusinessException(GlobalErrorMessages.ERROR_OCCURRED);
        }

    }

    public UserDto verifyAccount(String key) {
        User user = getUserByVerificationUrl(key);

        try {
            userEntityService.updateUserEnabledById(user.getId());

            UserDto dto = userConverter.toDto(user);

            return dto;
        } catch (Exception ex) {
            throw new BusinessException(GlobalErrorMessages.ERROR_OCCURRED);
        }

    }

    public UserDto updateUser(ProfileUpdateDto updateUser) {

        Optional<User> optionalUser = userEntityService.findById(updateUser.getId());

        return optionalUser.map(
                        user -> {
                            user.setFirstName(updateUser.getFirstName());
                            user.setLastName(updateUser.getLastName());
                            user.setEmail(updateUser.getEmail());
                            user.setPhone(updateUser.getPhone());
                            user.setAddress(updateUser.getAddress());
                            user.setTitle(updateUser.getTitle());
                            user.setBio(updateUser.getBio());

                            return userConverter.toDto(userEntityService.update(user));
                        })
                .orElse(null);
    }

    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new ApiException(UserErrorMessages.PASSWORD_NOT_EQUAL);
        }

        UserDto userById = getUserById(id);

        if (passwordEncoder.matches(currentPassword, userById.getPassword())) {
            userEntityService.updatePassword(id, passwordEncoder.encode(newPassword));
        } else {
            throw new ApiException(UserErrorMessages.PASSWORD_IS_WRONG);
        }

    }

    public UserDto getUserById(Long userId) {
        User user = userEntityService.findById(userId).orElse(null);
        return userConverter.toDto(user);
    }

    private Long isVerificationCodeExpired(String code) {
        return twoFactorVerificationService.isVerificationCodeExpiredByCode(code);
    }

    public UserDto findUserByVerificationCode(String code) {
        User user = userEntityService.findUserByVerificationCode(code);

        UserDto dto = userConverter.userAndRoleDto(user, roleService.getRoleByUserId(user.getId()));

        return dto;
    }

    private void userVerification(User user) {
        String verificationUrl = getVerificationUrl(getRandomUUID(), ACCOUNT.getType());
        userVerificationService.createUserVerification(user.getId(), verificationUrl);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto user = getUserByEmail(email);
        validateUser(user);

        return createUserDetails(user);
    }

    private UserPrincipal createUserDetails(UserDto user) {
        String permission = roleService.getRoleByUserId(user.getId()).getPermission();
        return new UserPrincipal(user, permission);
    }

    public UserDto getUserByEmail(String email) {
        User user = userEntityService.getUserByEmail(email);

        if (user == null) {
            log.error("User not found in the database");
            throw new ApiException(UserErrorMessages.USER_NOT_FOUND);
        }
        log.info("User found in the database: {}", email);

        UserDto userDto = userConverter.userAndRoleDto(user, roleService.getRoleByUserId(user.getId()));
        return userDto;
    }

    public void sendVerificationCode(UserDto user) {
        String expirationDate = getExpirationDate();
        String verificationCode = randomAlphabetic(8).toUpperCase();

        try {
            twoFactorVerificationService.deleteByUserId(user.getId());
            twoFactorVerificationService.updateByUserIdAndVerificationCodeAndExpirationDate(user.getId(), verificationCode, expirationDate);
            //sendSMS(user.getPhone(), "From: SecureInvoice \nVerification code\n" + verificationCode);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BusinessException(GlobalErrorMessages.ERROR_OCCURRED);
        }

    }

    public void renewPassword(String key, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) throw new ApiException(UserErrorMessages.PASSWORD_NOT_EQUAL);

        try {
            userEntityService.updatePasswordByUrl(passwordEncoder.encode(password), getVerificationUrl(key, PASSWORD.getType()));

        } catch (ApiException ex) {
            throw new BusinessException(GlobalErrorMessages.ERROR_OCCURRED);
        }


    }

    public UserDto verifyPasswordKey(String key) {

        if (isLinkExpired(key, PASSWORD) != 0) throw new ApiException(UserErrorMessages.CODE_EXPIRED);

        try {
            User user = userEntityService.findUserByResetPasswordVerification(getVerificationUrl(key, PASSWORD.getType()));
            UserDto dto = userConverter.toDto(user);

            return dto;
        } catch (BusinessException ex) {
            throw new BusinessException(GlobalErrorMessages.ERROR_OCCURRED);
        }
        // remove password reset link when user click the link once
        //resetPasswordVerificationsService.deleteResetPasswordVerificationsByUserId(user.getId());


    }

    // TODO Eğer key uygun formatte değilse direk exception çıkart

    private Long isLinkExpired(String key, VerificationType password) {
        Long isLinkValid = resetPasswordVerificationsService.isLinkExpired(getVerificationUrl(key, password.getType()));

        if (isLinkValid == null) throw new ApiException(UserErrorMessages.INVALID_KEY);

        try {
            return isLinkValid;
        } catch (Exception ex) {
            log.error("This link is not valid. URL **");
            throw new ApiException(UserErrorMessages.LINK_EXPIRED);
        }

    }

    private void validateUser(UserDto user) {
        if (!user.isEnable()) {
            log.error("USER DISABLED ERROR: {}", user.isEnable());
            throw new DisabledException("User is disabled");
        }
    }

    private String encodedUserPassword(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        return encodePassword;
    }

    private String getVerificationUrl(String key, String verificationType) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/verify/" + verificationType + "/" + key).toUriString();
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private String getExpirationDate() {
        String expirationDate = format(addDays(new Date(), 1), DATE_FORMAT);
        return expirationDate;
    }

    private User getUserByVerificationUrl(String key) {
        User user = userEntityService.findUserByVerificationUrl(getVerificationUrl(key, ACCOUNT.getType()));

        if (user == null) throw new ApiException(UserErrorMessages.INVALID_KEY);

        return user;
    }

    public void updateAccountSettings(Long userId, Boolean enable, Boolean isNotLocked) {
        userEntityService.updateAccountSettings(userId, enable, isNotLocked);
    }
}
