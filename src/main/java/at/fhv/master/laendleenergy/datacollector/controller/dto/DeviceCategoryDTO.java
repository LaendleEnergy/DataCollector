package at.fhv.master.laendleenergy.datacollector.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceCategoryDTO {

    @JsonProperty
    String name;

    public DeviceCategoryDTO(){

    }

    public DeviceCategoryDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
