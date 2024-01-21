package at.fhv.master.laendleenergy.datacollector.model.events.household;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceCategoryAddedEvent {
    @JsonProperty
    private String eventId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String deviceId;
    @JsonProperty
    private String householdId;
    @JsonProperty
    private String memberId;
    @JsonProperty
    private LocalDateTime timestamp;


    public DeviceCategoryAddedEvent(String name, String deviceId, String householdId, String memberId) {
        this.eventId = UUID.randomUUID().toString();
        this.name = name;
        this.deviceId = deviceId;
        this.householdId = householdId;
        this.memberId = memberId;
        this.timestamp = LocalDateTime.now();
    }

    public DeviceCategoryAddedEvent() {}

    public String getName() {
        return name;
    }

    public String getEventId() {
        return eventId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "DeviceCategoryAddedEvent{" +
                "eventId='" + eventId + '\'' +
                ", name='" + name + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}