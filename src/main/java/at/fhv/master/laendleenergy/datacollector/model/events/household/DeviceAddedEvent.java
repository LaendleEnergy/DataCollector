package at.fhv.master.laendleenergy.datacollector.model.events.household;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class DeviceAddedEvent {
    @JsonProperty
    private String eventId;
    @JsonProperty
    private String deviceName;
    @JsonProperty
    private String deviceCategoryName;
    @JsonProperty
    private String deviceId;
    @JsonProperty
    private LocalDateTime timestamp;

    public DeviceAddedEvent() {}

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceCategoryName() {
        return deviceCategoryName;
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
}
