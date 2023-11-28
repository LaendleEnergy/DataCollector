package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementService {
    void addTag(LocalDateTime startTime, LocalDateTime endTime, String deviceId, String caption, String deviceCategoryName) throws MeasurementNotFoundException, DeviceCategoryNotFoundException;
    List<MeasurementDTO> getMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
}
