package at.fhv.master.laendleenergy.datacollector.model;

import java.time.LocalDateTime;

public class AveragedMeasurement {
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String deviceId;
    private float currentL1AAvg;
    private float currentL2AAvg;
    private float currentL3AAvg;
    private float voltageL1VAvg;
    private float voltageL2VAvg;
    private float voltageL3VAvg;
    private float instantaneousActivePowerPlusWAvg;
    private float instantaneousActivePowerMinusWAvg;

    public AveragedMeasurement(LocalDateTime timeStart, LocalDateTime timeEnd, String deviceId, float currentL1AAvg,
                               float currentL2AAvg, float currentL3AAvg, float voltageL1VAvg,
                               float voltageL2VAvg, float voltageL3VAvg, float instantaneousActivePowerPlusWAvg,
                               float instantaneousActivePowerMinusWAvg) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.deviceId = deviceId;
        this.currentL1AAvg = currentL1AAvg;
        this.currentL2AAvg = currentL2AAvg;
        this.currentL3AAvg = currentL3AAvg;
        this.voltageL1VAvg = voltageL1VAvg;
        this.voltageL2VAvg = voltageL2VAvg;
        this.voltageL3VAvg = voltageL3VAvg;
        this.instantaneousActivePowerPlusWAvg = instantaneousActivePowerPlusWAvg;
        this.instantaneousActivePowerMinusWAvg = instantaneousActivePowerMinusWAvg;
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

    public float getCurrentL1AAvg() {
        return currentL1AAvg;
    }

    public float getCurrentL2AAvg() {
        return currentL2AAvg;
    }

    public float getCurrentL3AAvg() {
        return currentL3AAvg;
    }

    public float getVoltageL1VAvg() {
        return voltageL1VAvg;
    }

    public float getVoltageL2VAvg() {
        return voltageL2VAvg;
    }

    public float getVoltageL3VAvg() {
        return voltageL3VAvg;
    }

    public float getInstantaneousActivePowerPlusWAvg() {
        return instantaneousActivePowerPlusWAvg;
    }

    public float getInstantaneousActivePowerMinusWAvg() {
        return instantaneousActivePowerMinusWAvg;
    }
}
