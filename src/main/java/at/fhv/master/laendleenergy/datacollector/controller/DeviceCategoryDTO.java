package at.fhv.master.laendleenergy.datacollector.controller;

public class DeviceCategoryDTO {

    String name;
    String description;

    public DeviceCategoryDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
