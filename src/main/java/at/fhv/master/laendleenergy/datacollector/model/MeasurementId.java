package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class MeasurementId implements Serializable {

    @Column(name = "time", columnDefinition = "timestamptz")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time;
    @Column(name = "device_id")
    private int deviceId;


    public MeasurementId() {
    }

    public MeasurementId(int deviceId, LocalDateTime timestamp) {
        this.deviceId = deviceId;
        this.time = timestamp;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getTimestamp() {
        return time;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.time = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementId that = (MeasurementId) o;
        return Objects.equals(deviceId, that.deviceId) && that.time.equals(time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, time);
    }
}