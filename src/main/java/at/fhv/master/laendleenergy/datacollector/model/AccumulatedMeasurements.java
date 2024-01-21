package at.fhv.master.laendleenergy.datacollector.model;

import java.time.LocalDateTime;

public class AccumulatedMeasurements {
    private LocalDateTime timeStart;

    private LocalDateTime timeEnd;

    private String deviceId;

    private float energyConsumedWh;

    private float energyDeliveredWh;

    private float energyConsumedPriceEuro;

    public AccumulatedMeasurements(LocalDateTime timeStart, LocalDateTime timeEnd, String deviceId, float energyConsumedWh, float energyDeliveredWh, float energyConsumedPriceEuro) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.deviceId = deviceId;
        this.energyConsumedWh = energyConsumedWh;
        this.energyDeliveredWh = energyDeliveredWh;
        this.energyConsumedPriceEuro = energyConsumedPriceEuro;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setEnergyConsumedWh(float energyConsumedWh) {
        this.energyConsumedWh = energyConsumedWh;
    }

    public void setEnergyDeliveredWh(float energyDeliveredWh) {
        this.energyDeliveredWh = energyDeliveredWh;
    }

    public void setEnergyConsumedPriceEuro(float energyConsumedPriceEuro) {
        this.energyConsumedPriceEuro = energyConsumedPriceEuro;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public float getEnergyConsumedWh() {
        return energyConsumedWh;
    }

    public float getEnergyDeliveredWh() {
        return energyDeliveredWh;
    }

    public float getEnergyConsumedPriceEuro() {
        return energyConsumedPriceEuro;
    }
}
