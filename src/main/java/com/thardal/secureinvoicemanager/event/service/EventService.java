package com.thardal.secureinvoicemanager.event.service;

import com.thardal.secureinvoicemanager.event.dto.UserEventDetailsDto;
import com.thardal.secureinvoicemanager.event.entity.UserEvents;
import com.thardal.secureinvoicemanager.event.enums.EventType;
import com.thardal.secureinvoicemanager.event.service.entityservice.EventEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public List<UserEventDetailsDto> getUserEventsByUserId(Long userId) {
        List<Object[]> resultList = eventEntityService.getUserEventsByUserId(userId);
        return populateUserEvents(resultList);
    }

    public List<UserEventDetailsDto> populateUserEvents(List<Object[]> resultList) {
        List<UserEventDetailsDto> userEvents = new ArrayList<>();
        try {
            for (Object[] result : resultList) {
                UserEventDetailsDto userEventDetailsDto = new UserEventDetailsDto();
                userEventDetailsDto.setDevice((String) result[0]);
                userEventDetailsDto.setIpAddress((String) result[1]);
                userEventDetailsDto.setEventType((String) result[2]);
                userEventDetailsDto.setDescription((String) result[3]);
                userEvents.add(userEventDetailsDto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userEvents;
    }

    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        try {
            eventEntityService.addUserEvent(email, eventType.toString(), device, ipAddress);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
