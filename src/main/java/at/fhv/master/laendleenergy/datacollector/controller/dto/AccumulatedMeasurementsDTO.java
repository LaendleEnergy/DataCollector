package at.fhv.master.laendleenergy.datacollector.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class AccumulatedMeasurementsDTO {
    @JsonProperty
    private LocalDateTime timeStart;
    @JsonProperty
    private LocalDateTime timeEnd;
    @JsonProperty
    private String deviceId;
    @JsonProperty
    private float energyConsumedWh;
    @JsonProperty
    private float energyDeliveredWh;
    @JsonProperty
    private float energyConsumedPriceEuro;

    public AccumulatedMeasurementsDTO(LocalDateTime timeStart, LocalDateTime timeEnd, String deviceId, float energyConsumedWh, float energyDeliveredWh, float energyConsumedPriceEuro) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.deviceId = deviceId;
        this.energyConsumedWh = energyConsumedWh;
        this.energyDeliveredWh = energyDeliveredWh;
        this.energyConsumedPriceEuro = energyConsumedPriceEuro;
    }
}
