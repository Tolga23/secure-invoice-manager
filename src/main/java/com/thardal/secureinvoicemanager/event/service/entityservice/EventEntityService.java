package com.thardal.secureinvoicemanager.event.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.event.entity.UserEvents;
import com.thardal.secureinvoicemanager.event.enums.EventType;
import com.thardal.secureinvoicemanager.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EventEntityService extends BaseEntityService<UserEvents, EventRepository> {
    public EventEntityService(EventRepository dao) {
        super(dao);
    }

    public Collection<UserEvents> getUserEventByUserId(Long userId) {
        return getDao().getUserEventByUserId(userId);
    }

    public List<Object[]> getUserEventsByUserId(Long userId) {
        return getDao().getUserEventsByUserId(userId);
    }

    public void addUserEvent(String email, String eventType, String device, String ipAddress) {
        getDao().addUserEvent(email, eventType, device, ipAddress);
    }

    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {
        getDao().addUserEvent(userId, eventType, device, ipAddress);
    }
}
