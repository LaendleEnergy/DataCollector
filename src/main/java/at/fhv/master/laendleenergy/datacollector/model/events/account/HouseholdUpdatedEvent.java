package at.fhv.master.laendleenergy.datacollector.model.events.account;

import at.fhv.master.laendleenergy.datacollector.model.events.account.domain.UpdatedHousehold;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class HouseholdUpdatedEvent {
    @JsonProperty("eventId")
    private String eventId;
    @JsonProperty("household")
    private UpdatedHousehold updatedHousehold;
    @JsonProperty("timestamp")
    private LocalDateTime updateTime;

    public String getEventId() {
        return eventId;
    }

    public UpdatedHousehold getUpdatedHousehold() {
        return updatedHousehold;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public HouseholdUpdatedEvent(String eventId, UpdatedHousehold updatedHousehold, LocalDateTime updateTime) {
        this.eventId = eventId;
        this.updatedHousehold = updatedHousehold;
        this.updateTime = updateTime;
    }

    public HouseholdUpdatedEvent() {
    }
}
