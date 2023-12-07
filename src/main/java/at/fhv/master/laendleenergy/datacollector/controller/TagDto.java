package at.fhv.master.laendleenergy.datacollector.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;


public class TagDto {
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
}
