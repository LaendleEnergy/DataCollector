package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

//@Entity
public class DeviceCategory {

    @Id
    @Column(name = "category_name")
    String categoryName;
    @Column()
    String description;

    public DeviceCategory(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    public DeviceCategory() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }


}
