package at.fhv.master.laendleenergy.datacollector.controller.dto;
import java.time.LocalDateTime;




public class MeasurementDTO {
    String deviceId;
    LocalDateTime measurementTime;
    Float instantaneousActivePowerPlusW;
    //todo: add tag information


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(LocalDateTime measurementTime) {
        this.measurementTime = measurementTime;
    }

    public Float getInstantaneousActivePowerPlusW() {
        return instantaneousActivePowerPlusW;
    }

    public void setInstantaneousActivePowerPlusW(Float instantaneousActivePowerPlusW) {
        this.instantaneousActivePowerPlusW = instantaneousActivePowerPlusW;
    }
}
