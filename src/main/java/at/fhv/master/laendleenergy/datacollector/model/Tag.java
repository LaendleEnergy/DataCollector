package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;


//@Entity
public class Tag {

    String name;
    String deviceCategoryName;

    public Tag(String name, DeviceCategory deviceCategory, Measurement measurement) {
        this.name = name;
        this.deviceCategoryName = deviceCategory.categoryName;

    }

    public Tag(String name, String deviceCategoryName) {
        this.name = name;
        this.deviceCategoryName = deviceCategoryName;

    }

    protected Tag() { }

    public String getName() {
        return name;
    }

    public String getDeviceCategoryName() {
        return deviceCategoryName;
    }
}
