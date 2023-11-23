package at.fhv.master.laendleenergy.datacollector.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "measurement")
public class Measurement {


    @EmbeddedId
    MeasurementId measurementId;

    @Column(name = "current_l1a")
    private float currentL1A;
    @Column(name = "current_l2a")
    private float currentL2A;
    @Column(name = "current_l3a")
    private float currentL3A;
    @Column(name = "current_l1v")
    private float voltageL1V;
    @Column(name = "current_l2v")
    private float voltageL2V;
    @Column(name = "current_l3v")
    private float voltageL3V;
    @Column(name = "instantaneous_active_power_plus_w")
    private float instantaneousActivePowerPlusW;
    @Column(name = "instantaneous_active_power_minus_w")
    private float instantaneousActivePowerMinusW;
    @Column(name = "total_energy_consumed_wh")
    private float totalEnergyConsumedWh;
    @Column(name = "total_energy_delivered_wh")
    private float totalEnergyDeliveredWh;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "measurement")
    @Transient
    private List<Tag> tags = new ArrayList<>();;

    public Measurement() {

    }

    public Measurement(LocalDateTime timestamp, float currentL1A, float currentL2A, float currentL3A,
                       float voltageL1V, float voltageL2V, float voltageL3V,
                       float instantaneousActivePowerPlusW, float instantaneousActivePowerMinusW,
                       float totalEnergyConsumedWh, float totalEnergyDeliveredWh) {
        //todo: deviceId logic
        this.measurementId = new MeasurementId(1, timestamp);
        this.currentL1A = currentL1A;
        this.currentL2A = currentL2A;
        this.currentL3A = currentL3A;
        this.voltageL1V = voltageL1V;
        this.voltageL2V = voltageL2V;
        this.voltageL3V = voltageL3V;
        this.instantaneousActivePowerPlusW = instantaneousActivePowerPlusW;
        this.instantaneousActivePowerMinusW = instantaneousActivePowerMinusW;
        this.totalEnergyConsumedWh = totalEnergyConsumedWh;
        this.totalEnergyDeliveredWh = totalEnergyDeliveredWh;
    }

    public LocalDateTime getTimestamp() {
        return measurementId.getTimestamp();
    }


    public List<at.fhv.master.laendleenergy.datacollector.model.Tag> getTags() {
        return tags;
    }

    public float getCurrentL1A() {
        return currentL1A;
    }

    public float getCurrentL2A() {
        return currentL2A;
    }

    public float getCurrentL3A() {
        return currentL3A;
    }

    public float getVoltageL1V() {
        return voltageL1V;
    }

    public float getVoltageL2V() {
        return voltageL2V;
    }

    public float getVoltageL3V() {
        return voltageL3V;
    }

    public float getInstantaneousActivePowerPlusW() {
        return instantaneousActivePowerPlusW;
    }

    public float getInstantaneousActivePowerMinusW() {
        return instantaneousActivePowerMinusW;
    }

    public float getTotalEnergyConsumedWh() {
        return totalEnergyConsumedWh;
    }

    public float getTotalEnergyDeliveredWh() {
        return totalEnergyDeliveredWh;
    }

    public int getDeviceId() {
        return measurementId.getDeviceId();
    }

    public void setCurrentL1A(float currentL1A) {
        this.currentL1A = currentL1A;
    }

    public void setCurrentL2A(float currentL2A) {
        this.currentL2A = currentL2A;
    }

    public void setCurrentL3A(float currentL3A) {
        this.currentL3A = currentL3A;
    }

    public void setVoltageL1V(float voltageL1V) {
        this.voltageL1V = voltageL1V;
    }

    public void setVoltageL2V(float voltageL2V) {
        this.voltageL2V = voltageL2V;
    }

    public void setVoltageL3V(float voltageL3V) {
        this.voltageL3V = voltageL3V;
    }

    public void setInstantaneousActivePowerPlusW(float instantaneousActivePowerPlusW) {
        this.instantaneousActivePowerPlusW = instantaneousActivePowerPlusW;
    }

    public void setInstantaneousActivePowerMinusW(float instantaneousActivePowerMinusW) {
        this.instantaneousActivePowerMinusW = instantaneousActivePowerMinusW;
    }

    public void setTotalEnergyConsumedWh(float totalEnergyConsumedWh) {
        this.totalEnergyConsumedWh = totalEnergyConsumedWh;
    }

    public void setTotalEnergyDeliveredWh(float totalEnergyDeliveredWh) {
        this.totalEnergyDeliveredWh = totalEnergyDeliveredWh;
    }

    @Override
    public String toString() {
        return "MQTTMessage{" +
                "timestamp=" + measurementId.getTimestamp() +
                ", currentL1A=" + currentL1A +
                ", currentL2A=" + currentL2A +
                ", currentL3A=" + currentL3A +
                ", voltageL1V=" + voltageL1V +
                ", voltageL2V=" + voltageL2V +
                ", voltageL3V=" + voltageL3V +
                ", instantaneousActivePowerPlusW=" + instantaneousActivePowerPlusW +
                ", instantaneousActivePowerMinusW=" + instantaneousActivePowerMinusW +
                ", totalEnergyConsumedWh=" + totalEnergyConsumedWh +
                ", totalEnergyDeliveredWh=" + totalEnergyDeliveredWh +
                '}';
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        return Objects.equals(measurementId, that.measurementId);
    }

    @Override
    public int hashCode() {
        return measurementId != null ? measurementId.hashCode() : 0;
    }
}
