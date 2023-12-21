package at.fhv.master.laendleenergy.datacollector.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;


public class TagDTO {
    //@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startTime;
    //@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endTime;
    @JsonProperty
    String caption;
    @JsonProperty
    String deviceCategoryName;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getCaption() {
        return caption;
    }

    public String getDeviceCategoryName() {
        return deviceCategoryName;
    }
}
