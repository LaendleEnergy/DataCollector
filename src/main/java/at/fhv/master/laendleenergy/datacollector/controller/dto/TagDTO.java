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
    @JsonProperty(value = "caption")
    String deviceName;
    @JsonProperty
    String deviceCategoryName;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceCategoryName() {
        return deviceCategoryName;
    }

    public TagDTO() {
    }

    public TagDTO(LocalDateTime startTime, LocalDateTime endTime, String deviceName, String deviceCategoryName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.deviceName = deviceName;
        this.deviceCategoryName = deviceCategoryName;
    }
}
