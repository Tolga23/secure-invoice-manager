package com.thardal.secureinvoicemanager.user.controller;

import com.thardal.secureinvoicemanager.base.entity.HttpResponse;
import com.thardal.secureinvoicemanager.role.service.RoleService;
import com.thardal.secureinvoicemanager.security.provider.TokenProvider;
import com.thardal.secureinvoicemanager.user.dto.*;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import com.thardal.secureinvoicemanager.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity findAll() {
        List<UserDto> userDtoList = userService.findAll();

        return ResponseEntity.created(getUri()).body(userDtoList);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid UserSaveRequestDto userSaveRequestDto) {
        UserDto userDto = userService.save(userSaveRequestDto);

        return ResponseEntity.ok(HttpResponse.of(CREATED,"Registered successfully.", Map.of("user", userDto)));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto userLoginDto) {

        Authentication authentication = authenticate(userLoginDto.getEmail(), userLoginDto.getPassword());
        UserDto user = getLoggedUser(authentication);
        return user.isUsingAuth() ? sendVerificationCode(user) : sendResponse(user);
    }

    @PatchMapping("/update")
    public ResponseEntity updateUser(@RequestBody @Valid ProfileUpdateDto user) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        UserDto updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(HttpResponse.of(OK, "User updated", Map.of("user", updatedUser)));
    }

    @GetMapping("/profile")
    public ResponseEntity profile(Authentication authentication) {
        UserDto user = userService.getUserByEmail(getAuthenticatedUser(authentication).getEmail());

        return ResponseEntity.ok(HttpResponse.of(OK, "Profile Retrieved", Map.of("user", user,"roles",roleService.getRoles())));
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

    @PostMapping("/resetpassword/{key}/{password}/{confirmPassword}")
    public ResponseEntity resetPassword(@PathVariable("key") String key, @PathVariable("password") String password, @PathVariable("confirmPassword") String confirmPassword) {
        userService.renewPassword(key, password, confirmPassword);

        return ResponseEntity.ok(HttpResponse.of(OK, "Password successfully changed."));
    }

    @PatchMapping("/update/password")
    public ResponseEntity updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordDto user) {
        UserDto userDto = getAuthenticatedUser(authentication);

        userService.updatePassword(userDto.getId(), user.getCurrentPassword(), user.getNewPassword(), user.getConfirmPassword());
        return ResponseEntity.ok(HttpResponse.of(OK, "Password successfully changed."));
    }

    @PatchMapping("/update/role/{roleName}")
    public ResponseEntity updateRole(Authentication authentication, @PathVariable("roleName") String roleName) {
        UserDto user = userService.getUserByEmail(getAuthenticatedUser(authentication).getEmail());
        roleService.updateRoleToUser(user.getId(),roleName);
        return ResponseEntity.ok(HttpResponse.of(OK,"Role successfully updated.",Map.of("user", userService.getUserByEmail(user.getEmail()),"roles",roleService.getRoles())));
    }

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDto user = userService.verifyCode(email, code);

        return ResponseEntity.ok(HttpResponse.of(OK, "Login Success", Map.of("user", user,
                "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user)))));
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
            UserDto user = userService.getUserById(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok(HttpResponse.of(OK, "Token refreshed", Map.of("user", user,
                    "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                    "refresh_token", token)));
        } else {
            return ResponseEntity.badRequest().body(HttpResponse.error(BAD_REQUEST, "Refresh Token missing or invalid", "Refresh Token missing or invalid"));
        }
    }

    private boolean isHeaderTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && tokenProvider.isTokenValid(tokenProvider.getSubject(request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()));
    }

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(HttpResponse.error(BAD_REQUEST, "There is no mapping for a " + request.getMethod() + " request for this path on the server"));
    }

    private ResponseEntity sendResponse(UserDto user) {
        userService.sendVerificationCode(user);

        return ResponseEntity.ok(HttpResponse.of(OK, "Login Success", Map.of("user", user,
                "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user)))));
    }

    private ResponseEntity sendVerificationCode(UserDto user) {
        userService.sendVerificationCode(user);

        return ResponseEntity.ok(HttpResponse.of(OK, "Verification Code Sent", Map.of("user", user)));
    }

    private UserPrincipal getUserPrincipal(UserDto user) {
        return new UserPrincipal(user,
                roleService.getRoleByUserId(user.getId()).getRoleName());
    }

    private UserDto getAuthenticatedUser(Authentication authentication) {
        return ((UserDto) authentication.getPrincipal());
    }

    private UserDto getLoggedUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }

    private Authentication authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
        return authentication;
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}


