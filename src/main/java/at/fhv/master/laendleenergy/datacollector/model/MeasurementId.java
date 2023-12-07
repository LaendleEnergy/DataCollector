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

    @Column(name = "timestamp", columnDefinition = "timestamptz")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;
    @Column(name = "device_id")
    private String deviceId;


    public MeasurementId() {
    }

    public MeasurementId(String deviceId, LocalDateTime timestamp) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementId that = (MeasurementId) o;
        return Objects.equals(deviceId, that.deviceId) && that.timestamp.equals(timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, timestamp);
    }
}