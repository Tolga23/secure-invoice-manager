package com.thardal.secureinvoicemanager.event.controller;

import com.thardal.secureinvoicemanager.base.entity.HttpResponse;
import com.thardal.secureinvoicemanager.event.entity.UserEvents;
import com.thardal.secureinvoicemanager.event.service.EventService;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity getEventsByUserId(Authentication authentication){
        UserDto user = authService.getAuthenticatedUser(authentication);
        Collection<UserEvents> eventsByUserId = eventService.getEventsByUserId(user.getId());
        return ResponseEntity.ok(HttpResponse.of(OK,"User events retrieved.", Map.of("events",eventsByUserId)));
    }

//    @PostMapping
//    public ResponseEntity addEvent(Authentication authentication){
//        UserDto user = authService.getAuthenticatedUser(authentication);
//        eventService.addEvent(user.getEmail(), EventType.LOGIN_ATTEMPT, "Device","IP Address");
//        return ResponseEntity.ok(HttpResponse.of(OK,"User event added"));
//    }
}
