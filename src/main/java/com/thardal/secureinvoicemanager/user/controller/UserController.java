package com.thardal.secureinvoicemanager.user.controller;

import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity findAll(){
        List<UserDto> userDtoList = userService.findAll();

        return ResponseEntity.ok(userDtoList);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody UserSaveRequestDto userSaveRequestDto){
        UserDto userDto = userService.save(userSaveRequestDto);

        return ResponseEntity.ok(userDto);
    }
}
