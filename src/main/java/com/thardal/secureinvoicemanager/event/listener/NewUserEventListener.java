package com.thardal.secureinvoicemanager.event.listener;

import com.thardal.secureinvoicemanager.event.entity.NewUserEvent;
import com.thardal.secureinvoicemanager.event.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.thardal.secureinvoicemanager.base.utils.RequestUtils.getDevice;
import static com.thardal.secureinvoicemanager.base.utils.RequestUtils.getIpAddress;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserEventListener {
    private final EventService eventService;
    private final HttpServletRequest request;

    @EventListener
    public void onNewUserEvent(NewUserEvent event){
        log.info("NewUserEvent is fired");
        eventService.addUserEvent(event.getEmail(),event.getType(),getDevice(request),getIpAddress(request));
    }
}
