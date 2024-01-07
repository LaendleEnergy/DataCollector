package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.dto.AccumulatedMeasurementsDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.AverageMeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementService {
    void addTag(LocalDateTime startTime, LocalDateTime endTime, String caption, String deviceCategoryName) throws MeasurementNotFoundException, DeviceCategoryNotFoundException, JsonProcessingException;
    List<AverageMeasurementDTO> getAveragedMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, int numberGroups);

    List<AverageMeasurementDTO> getAveragedMeasurementsBetweenDatesAndInterval(LocalDateTime startDate, LocalDateTime endDate, String interval);

    List<AccumulatedMeasurementsDTO> getAccumulatedMeasurementsBetweenDatesAndInterval(LocalDateTime startDate, LocalDateTime endDate, String interval);

    List<MeasurementDTO> getMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<String> getAllTagNames();
}
