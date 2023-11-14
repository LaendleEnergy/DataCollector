package at.fhv.master.laendleenergy.datacollector;

import java.time.LocalDateTime;

public class MQTTMessage {
    private LocalDateTime timestamp;
    private float currentL1A;
    private float currentL2A;
    private float currentL3A;
    private float voltageL1V;
    private float voltageL2V;
    private float voltageL3V;
    private float instantaneousActivePowerPlusW;
    private float instantaneousActivePowerMinusW;
    private float totalEnergyConsumedWh;
    private float totalEnergyDeliveredWh;

    public MQTTMessage(LocalDateTime timestamp, float currentL1A, float currentL2A, float currentL3A,
                       float voltageL1V, float voltageL2V, float voltageL3V,
                       float instantaneousActivePowerPlusW, float instantaneousActivePowerMinusW,
                       float totalEnergyConsumedWh, float totalEnergyDeliveredWh) {
        this.timestamp = timestamp;
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
        return timestamp;
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

    @Override
    public String toString() {
        return "MQTTMessage{" +
                "timestamp=" + timestamp +
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
}
