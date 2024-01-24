package at.fhv.master.laendleenergy.datacollector.model.events.household;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class DeviceAddedEvent {
    @JsonProperty
    private String eventId;
    @JsonProperty("name")
    private String deviceName;
    @JsonProperty("categoryName")
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
