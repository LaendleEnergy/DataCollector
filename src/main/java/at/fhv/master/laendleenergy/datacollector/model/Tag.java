package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Tag {
    @Id
    String name;
    @Id
    @ManyToOne
    DeviceCategory deviceCategory;
    @Id
    @Column(name = "fk_device_id")
    String deviceId;
    @Id
    @Column(name = "fk_measurement_timestamp")
    LocalDateTime measurementTimeStamp;

    public Tag(String name, DeviceCategory deviceCategory, String deviceId, LocalDateTime measurementTimeStamp) {
        this.name = name;
        this.deviceCategory = deviceCategory;
        this.deviceId = deviceId;
        this.measurementTimeStamp = measurementTimeStamp;
    }

    protected Tag() { }
}
