package at.fhv.master.laendleenergy.datacollector.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AverageMeasurementDTO {
    @JsonProperty
    private LocalDateTime timeStart;
    @JsonProperty
    private LocalDateTime timeEnd;
    @JsonProperty
    private String deviceId;
    @JsonProperty
    private float currentL1AAvg;
    @JsonProperty
    private float currentL2AAvg;
    @JsonProperty
    private float currentL3AAvg;
    @JsonProperty
    private float voltageL1VAvg;
    @JsonProperty
    private float voltageL2VAvg;
    @JsonProperty
    private float voltageL3VAvg;
    @JsonProperty
    private float instantaneousActivePowerPlusWAvg;
    @JsonProperty
    private float instantaneousActivePowerMinusWAvg;

    public AverageMeasurementDTO(LocalDateTime timeStart, LocalDateTime timeEnd, String deviceId, float currentL1AAvg,
                                 float currentL2AAvg, float currentL3AAvg, float voltageL1VAvg, float voltageL2VAvg,
                                 float voltageL3VAvg, float instantaneousActivePowerPlusWAvg, float instantaneousActivePowerMinusWAvg) {
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
