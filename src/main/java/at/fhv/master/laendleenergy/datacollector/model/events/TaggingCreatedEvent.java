package at.fhv.master.laendleenergy.datacollector.model.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaggingCreatedEvent {

    private String eventId;

    private LocalDateTime taggingTime;
    private String userId;
    private String deviceId;
    private String householdId;


    public TaggingCreatedEvent(String userId, String deviceId, String householdId) {
        eventId = UUID.randomUUID().toString();
        this.taggingTime = LocalDateTime.now();
        this.userId = userId;
        this.deviceId = deviceId;
        this.householdId = householdId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getTaggingTime() {
        return taggingTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getHouseholdId() {
        return householdId;
    }
}
