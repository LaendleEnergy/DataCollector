package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;


//@Entity
public class Tag {
    @Id
    String name;
    @Id
    @ManyToOne
    DeviceCategory deviceCategory;
    //@ManyToOne
    //Measurement measurement;
    @Id
    @Column(name = "measurement_reading_time")
    LocalDateTime measurementReadingTime;
    @Id
    String measurementDeviceId;

    public Tag(String name, DeviceCategory deviceCategory, Measurement measurement) {
        this.name = name;
        this.deviceCategory = deviceCategory;
        this.measurementReadingTime = measurement.getTimestamp();
        this.measurementDeviceId = measurement.getDeviceId();
    }

    protected Tag() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!Objects.equals(name, tag.name)) return false;
        if (!Objects.equals(deviceCategory, tag.deviceCategory))
            return false;
        if (!Objects.equals(measurementDeviceId, tag.measurementDeviceId))
            return false;
        return Objects.equals(measurementReadingTime, tag.measurementReadingTime);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (deviceCategory != null ? deviceCategory.hashCode() : 0);
        result = 31 * result + (measurementReadingTime != null ? measurementReadingTime.hashCode() : 0);
        result = 31 * result + (measurementDeviceId != null ? measurementDeviceId.hashCode() : 0);
        return result;
    }
}
