package at.fhv.master.laendleenergy.datacollector.controller.dto;
import java.time.LocalDateTime;
import java.util.List;


public class MeasurementDTO {
    String deviceId;
    LocalDateTime measurementTime;
    Float instantaneousActivePowerPlusW;


    List<TagDTO> tagDTOList;


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

    public List<TagDTO> getTagDTOList() {
        return tagDTOList;
    }

    public void setInstantaneousActivePowerPlusW(Float instantaneousActivePowerPlusW) {
        this.instantaneousActivePowerPlusW = instantaneousActivePowerPlusW;
    }

    public void setTagDTOList(List<TagDTO> tagDTOList) {
        this.tagDTOList = tagDTOList;
    }
}
