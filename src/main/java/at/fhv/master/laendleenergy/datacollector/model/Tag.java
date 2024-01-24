package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "tag_view")
public class Tag {
    @Id
    @Column(name = "device_name")
    String deviceName;

    @Id
    @Column(name = "meter_device_id")
    String meterDeviceId;

    @Column(name = "devicecategory_category_name")
    String deviceCategoryName;

    @Id
    @Column(name = "time_start")
    LocalDateTime starTime;

    @Id
    @Column(name = "time_end")
    LocalDateTime endTime;

    @Transient
    List<Measurement> measurements;

    public Tag(String deviceName, DeviceCategory deviceCategory, LocalDateTime startTime, LocalDateTime endTime, String meterDeviceId, List<Measurement> measurements) {
        this.deviceName = deviceName;
        this.deviceCategoryName = deviceCategory.categoryName;
        this.starTime = startTime;
        this.endTime = endTime;
        this.measurements = measurements;
        this.meterDeviceId = meterDeviceId;
    }

    public Tag(String deviceName, String deviceCategoryName) {
        this.deviceName = deviceName;
        this.deviceCategoryName = deviceCategoryName;

    }

    protected Tag() { }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceCategoryName() {
        return deviceCategoryName;
    }

    public LocalDateTime getStarTime() {
        return starTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
