package com.thardal.secureinvoicemanager.event.service;

import com.thardal.secureinvoicemanager.event.entity.UserEvents;
import com.thardal.secureinvoicemanager.event.enums.EventType;
import com.thardal.secureinvoicemanager.event.service.entityservice.EventEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class EventService {

    private EventEntityService eventEntityService;

    public EventService(EventEntityService eventEntityService) {
        this.eventEntityService = eventEntityService;
    }

    public Collection<UserEvents> getEventsByUserId(Long userId) {
        return eventEntityService.getUserEventByUserId(userId);
    }

    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        try {
            eventEntityService.addUserEvent(email, eventType.toString(), device, ipAddress);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
