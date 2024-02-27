package com.thardal.secureinvoicemanager.event.repository;

import com.thardal.secureinvoicemanager.event.entity.UserEvents;
import com.thardal.secureinvoicemanager.event.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional
@Repository
public interface EventRepository extends JpaRepository<UserEvents,Long> {
    Collection<UserEvents> getUserEventByUserId(Long userId);

    @Modifying
    @Query(value =  "INSERT INTO user_event (user_id, event_id, device, ip_address) " +
            "VALUES ((SELECT id FROM Users WHERE email = :email)," +
            " (SELECT id FROM Events WHERE type = :eventType)," +
            " :device, :ipAddress)", nativeQuery = true)
    void addUserEvent(@Param("email") String email,@Param("eventType") String eventType,  @Param("device") String device, @Param("ipAddress") String ipAddress);

    @Modifying
    @Query(value = "INSERT INTO user_event (user_id, event_id, device, ip_address)" +
            " VALUES (:userId, " +
            "(select events.id from events where type = :eventType), " +
            ":device, :ipAddress)", nativeQuery = true)
    void addUserEvent(@Param("userId") Long userId, @Param("eventType") EventType eventType, @Param("device") String device, @Param("ipAddress") String ipAddress);
}
