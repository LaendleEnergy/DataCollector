package at.fhv.master.laendleenergy.datacollector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class MeasurementId implements Serializable {
    @Column(name = "reading_time", columnDefinition = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;
    @Column(name = "device_id")
    @JsonProperty("deviceId")
    private String meterDeviceId;


    public MeasurementId() {
    }

    public MeasurementId(String meterDeviceId, LocalDateTime timestamp) {
        this.meterDeviceId = meterDeviceId;
        this.timestamp = timestamp;
    }

    public String getMeterDeviceId() {
        return meterDeviceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setMeterDeviceId(String deviceId) {
        this.meterDeviceId = deviceId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementId that = (MeasurementId) o;
        return Objects.equals(meterDeviceId, that.meterDeviceId) && that.timestamp.equals(timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meterDeviceId, timestamp);
    }
}