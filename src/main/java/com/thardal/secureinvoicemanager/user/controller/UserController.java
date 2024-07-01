package com.thardal.secureinvoicemanager.user.controller;

import com.thardal.secureinvoicemanager.base.entity.HttpResponse;
import com.thardal.secureinvoicemanager.event.service.EventService;
import com.thardal.secureinvoicemanager.role.service.RoleService;
import com.thardal.secureinvoicemanager.user.dto.*;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import com.thardal.secureinvoicemanager.user.service.AuthService;
import com.thardal.secureinvoicemanager.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.thardal.secureinvoicemanager.security.constants.Constants.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final AuthService authService;
    private final EventService eventService;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity findAll() {
        List<UserDto> userDtoList = userService.findAll();

        return ResponseEntity.created(getUri()).body(userDtoList);
    }

    @PostMapping("/register")
    public ResponseEntity save(@RequestBody @Valid UserSaveRequestDto userSaveRequestDto) {
        UserDto userDto = userService.save(userSaveRequestDto);

        return ResponseEntity.ok(HttpResponse.of(CREATED, String.format("Registered successfully for" +
                " %s",userDto.getFirstName()), Map.of("user", userDto)));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto userLoginDto) {

        Authentication authentication = authService.authenticate(userLoginDto.getEmail(), userLoginDto.getPassword());
        UserDto user = authService.getLoggedUser(authentication);

//        if (user != null) publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.LOGIN_ATTEMPT));

        return user.isUsingAuth() ? sendVerificationCode(user) : sendResponse(user);
    }

    @PatchMapping("/update")
    public ResponseEntity updateUser(@RequestBody @Valid ProfileUpdateDto user) {
        UserDto userDto = userService.updateUser(user);
//        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.PROFILE_UPDATE));
        return ResponseEntity.ok(HttpResponse.of(OK, "User updated", Map.of("user", userService.getUserAndRolesByUserId(userDto.getId()),"events", eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles())));
    }

    @PatchMapping("/update/settings")
    public ResponseEntity updateAccountSettings(Authentication authentication, @RequestBody @Valid AccountSettingsDto form) {
        UserDto userDto = authService.getAuthenticatedUser(authentication);
        userService.updateAccountSettings(userDto.getId(), form.getEnable(), form.getIsNotLocked());
//        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.ACCOUNT_SETTINGS_UPDATE));
        return ResponseEntity.ok(HttpResponse.of(OK, "Account setting updated.", Map.of("user", userService.getUserAndRolesByUserId(userDto.getId()),"events",eventService.getUserEventsByUserId(userDto.getId()), "roles", roleService.getRoles())));
    }

    @PatchMapping("/update/2fa")
    public ResponseEntity toggleTwoFactorVerification(Authentication authentication) {
        UserDto userDto = userService.toggleTwoFactorVerification(authService.getAuthenticatedUser(authentication).getEmail());
//        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.MFA_UPDATE));

        return ResponseEntity.ok(HttpResponse.of(OK, "Two factor verification updated.", Map.of("user",userDto,"events",eventService.getUserEventsByUserId(userDto.getId()), "roles", roleService.getRoles())));
    }

    @PatchMapping("/update/image")
    public ResponseEntity updateProfileImage(Authentication authentication, @RequestParam("image") MultipartFile image) {
        UserDto user = authService.getAuthenticatedUser(authentication);
        userService.updateProfileImage(user, image);
//        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.PROFILE_PICTURE_UPDATE));

        return ResponseEntity.ok(HttpResponse.of(OK, "Profile image updated.", Map.of("user", userService.getUserAndRolesByUserId(user.getId()),"events",eventService.getUserEventsByUserId(user.getId()), "roles", roleService.getRoles())));
    }

    @GetMapping(value = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public ResponseEntity getProfileImage(@PathVariable("fileName") String fileName) throws IOException {
        Path imagePath = Paths.get(System.getProperty("user.home") + "/Downloads/SecurePhotos/" + fileName);
        return Files.readAllBytes(imagePath).length > 0 ?
                ResponseEntity.ok(Files.readAllBytes(imagePath)) :
                ResponseEntity.badRequest().body(HttpResponse.error(BAD_REQUEST, "Image not found."));
    }

    @GetMapping("/profile")
    public ResponseEntity profile(Authentication authentication) {
        UserDto userDto = userService.getUserByEmail(authService.getAuthenticatedUser(authentication).getEmail());

        return ResponseEntity.ok(HttpResponse.of(OK, "Profile Retrieved", Map.of("user", userService.getUserAndRolesByUserId(userDto.getId()),"events", eventService.getUserEventsByUserId(userDto.getId()), "roles", roleService.getRoles())));
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity resetPasswordByMail(@PathVariable("email") String email) {
        userService.resetPassword(email);

        return ResponseEntity.ok(HttpResponse.of(OK, "Email sent. Please check your email to reset your password."));
    }

    @GetMapping("/verify/password/{key}")
    public ResponseEntity verifyPasswordUrl(@PathVariable("key") String key) {
        UserDto userDto = userService.verifyPasswordKey(key);

        return ResponseEntity.ok(HttpResponse.of(OK, "Please enter a new password.", Map.of("user", userDto)));
    }

    @PutMapping("/resetpassword")
    public ResponseEntity resetPassword(@RequestBody @Valid NewPasswordForm newPasswordForm) {
        userService.renewPassword(newPasswordForm.getUserId(), newPasswordForm.getPassword(), newPasswordForm.getConfirmPassword());

        return ResponseEntity.ok(HttpResponse.of(OK, "Password successfully changed."));
    }

    @PatchMapping("/update/password")
    public ResponseEntity updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordDto user) {
        UserDto userDto = authService.getAuthenticatedUser(authentication);
        userService.updatePassword(userDto.getId(), user.getCurrentPassword(), user.getNewPassword(), user.getConfirmPassword());
//        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.PASSWORD_UPDATE));
        return ResponseEntity.ok(HttpResponse.of(OK, "Password successfully changed.",Map.of("user", userService.getUserAndRolesByUserId(userDto.getId()),"events", eventService.getUserEventsByUserId(userDto.getId()), "roles", roleService.getRoles())));
    }

    @PatchMapping("/update/role/{roleName}")
    public ResponseEntity updateRole(Authentication authentication, @PathVariable("roleName") String roleName) {
        UserDto user = userService.getUserByEmail(authService.getAuthenticatedUser(authentication).getEmail());
        roleService.updateRoleToUser(user.getId(), roleName);
//        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.ROLE_UPDATE));
        return ResponseEntity.ok(HttpResponse.of(OK, "Role successfully updated.", Map.of("user", userService.getUserByEmail(user.getEmail()), "roles", roleService.getRoles())));
    }

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDto user = userService.verifyCode(email, code);

        return ResponseEntity.ok(HttpResponse.of(OK, "Login Success", Map.of("user", user,
                "access_token", authService.createAccessToken(getUserPrincipal(user)),
                "refresh_token", authService.createRefreshToken(getUserPrincipal(user)))));
    }

    @GetMapping("/verify/account/{key}")
    public ResponseEntity verifyAccount(@PathVariable("key") String key) {
        UserDto user = userService.verifyAccount(key);

        return ResponseEntity.ok(HttpResponse.of(OK, user.isEnable() ? "Account already verified" : "Account verified"));
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if (isHeaderTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
            UserDto user = userService.getUserById(authService.getSubject(token, request));
            return ResponseEntity.ok(HttpResponse.of(OK, "Token refreshed", Map.of("user", user,
                    "access_token", authService.createAccessToken(getUserPrincipal(user)),
                    "refresh_token", token)));
        } else {
            return ResponseEntity.badRequest().body(HttpResponse.error(BAD_REQUEST, "Refresh Token missing or invalid", "Refresh Token missing or invalid"));
        }
    }

    private boolean isHeaderTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && authService.isTokenValid(authService.getSubject(request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()));
    }

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(HttpResponse.error(BAD_REQUEST, "There is no mapping for a " + request.getMethod() + " request for this path on the server"));
    }

    private ResponseEntity sendResponse(UserDto user) {
        userService.sendVerificationCode(user);
//        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.LOGIN_ATTEMPT_SUCCESS));
        return ResponseEntity.ok(HttpResponse.of(OK, "Login Success", Map.of("user", user,
                "access_token", authService.createAccessToken(getUserPrincipal(user)),
                "refresh_token", authService.createRefreshToken(getUserPrincipal(user)))));
    }

    private ResponseEntity sendVerificationCode(UserDto user) {
        userService.sendVerificationCode(user);

        return ResponseEntity.ok(HttpResponse.of(OK, "Verification Code Sent", Map.of("user", user)));
    }

    private UserPrincipal getUserPrincipal(UserDto user) {
        return new UserPrincipal(user,
                roleService.getRoleByUserId(user.getId()));
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}


