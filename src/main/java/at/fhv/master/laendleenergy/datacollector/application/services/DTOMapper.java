package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;

public class DTOMapper {
    public static MeasurementDTO mapMeasurementToMeasurementDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setDeviceId(measurement.getDeviceId() + "");
        measurementDTO.setMeasurementTime(measurement.getTimestamp());
        measurementDTO.setInstantaneousActivePowerPlusW(measurement.getInstantaneousActivePowerPlusW());
        return measurementDTO;
    }
}
