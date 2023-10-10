package com.thardal.secureinvoicemanager.user.controller;

import com.thardal.secureinvoicemanager.base.entity.HttpResponse;
import com.thardal.secureinvoicemanager.role.service.RoleService;
import com.thardal.secureinvoicemanager.security.provider.TokenProvider;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserLoginDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import com.thardal.secureinvoicemanager.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
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
    public ResponseEntity save(@RequestBody UserSaveRequestDto userSaveRequestDto) {
        UserDto userDto = userService.save(userSaveRequestDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto userLoginDto) {
        authenticationManager.authenticate(unauthenticated(userLoginDto.getEmail(), userLoginDto.getPassword()));
        UserDto user = userService.getUserByEmail(userLoginDto.getEmail());
        return user.isUsingAuth() ? sendVerificationCode(user) : sendResponse(user);

    }

    @GetMapping("/profile")
    public ResponseEntity profile(Authentication authentication) {
        UserDto user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user))
                        .message("Profile Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDto user = userService.verifyCode(email,code);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user,
                                "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private ResponseEntity sendResponse(UserDto user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user,
                                "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private UserPrincipal getUserPrincipal(UserDto user) {
        return new UserPrincipal(userService.getUserByEmail(user.getEmail()),
                roleService.getRoleByUserId(user.getId()).getPermission());
    }

    private ResponseEntity sendVerificationCode(UserDto user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user))
                        .message("Verification Code Sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}


