package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.*;

@Entity
@Table(name="device")
public class Device {
    @Column(name = "device_name")
    @Id
    private String name;
    @Id
    @Column(name = "meter_device_id")
    private String meterDeviceId;
    @ManyToOne
    @JoinColumn(name = "category_name")
    private DeviceCategory deviceCategory;

    public Device() {
    }

    public Device(String name, String meterDeviceId, DeviceCategory deviceCategory) {
        this.name = name;
        this.meterDeviceId = meterDeviceId;
        this.deviceCategory = deviceCategory;
    }
}
