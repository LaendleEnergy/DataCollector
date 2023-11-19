package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.*;


@Entity
public class Tag {
    @Id
    String name;
    @Id
    @ManyToOne
    DeviceCategory deviceCategory;
    @Id
    @ManyToOne
    Measurement measurement;

    public Tag(String name, DeviceCategory deviceCategory, Measurement measurement) {
        this.name = name;
        this.deviceCategory = deviceCategory;
        this.measurement = measurement;
    }

    protected Tag() { }
}
