package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DeviceCategory {

    @Id
    @Column(name = "category_name")
    String categoryName;


    public DeviceCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public DeviceCategory() {
    }

    public String getCategoryName() {
        return categoryName;
    }

}
