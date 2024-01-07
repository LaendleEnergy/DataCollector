package at.fhv.master.laendleenergy.datacollector.application.services.impl;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.application.services.DTOMapper;
import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.controller.dto.AccumulatedMeasurementsDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.AverageMeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.*;
import at.fhv.master.laendleenergy.datacollector.model.events.TaggingCreatedEvent;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.repositories.AccumulatedMeasurementRepository;
import at.fhv.master.laendleenergy.datacollector.model.repositories.AverageMeasurementRepository;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceCategoryRepository;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import at.fhv.master.laendleenergy.datacollector.streams.publisher.TaggingCreatedEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class MeasurementServiceImpl implements MeasurementService {


    @Inject
    MeasurementRepository measurementRepository;

    @Inject
    AverageMeasurementRepository averageMeasurementRepository;

    @Inject
    AccumulatedMeasurementRepository accumulatedMeasurementRepository;

    @Inject
    TaggingCreatedEventPublisher taggingCreatedEventPublisher;

    @Inject
    DeviceCategoryRepository deviceCategoryRepository;

    @Override
    @Transactional
    public void addTag(LocalDateTime startTime, LocalDateTime endTime,
                       String caption, String deviceCategoryName) throws MeasurementNotFoundException, DeviceCategoryNotFoundException, JsonProcessingException {

        //todo fetch deviceid from session
        List<Measurement> measurements = measurementRepository.getMeasurementsByDeviceIdAndStartAndEndTime("1", startTime, endTime);

        if(measurements.isEmpty()){
            throw new MeasurementNotFoundException();
        }

        Optional<DeviceCategory> deviceCategoryOptional = deviceCategoryRepository.getDeviceCategoryByName(deviceCategoryName);

        if(deviceCategoryOptional.isEmpty()){
            throw new DeviceCategoryNotFoundException();
        }

        for(Measurement measurement : measurements){
            Tag tag = new Tag(caption, deviceCategoryOptional.get(), measurement);
            measurement.addTag(tag);
            measurementRepository.saveChanges(measurement);
            taggingCreatedEventPublisher.publishMessage(new TaggingCreatedEvent(
                    "1", "1", "1"
            ));
        }
    }

    @Override
    public List<AverageMeasurementDTO> getAveragedMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, int numberOfGroups) {
        //todo: retrieve deviceId from session (?)
        List<AveragedMeasurement> measurements = averageMeasurementRepository.getNAveragedMeasurementsByDeviceIdAndStartAndEndTime(
                "1", startDate, endDate, numberOfGroups
        );
        //return Collections.emptyList();
        return measurements.stream().map(DTOMapper::mapAverageMeasurmentToAverageMeasurementDTO).toList();
    }

    @Override
    public List<AverageMeasurementDTO> getAveragedMeasurementsBetweenDatesAndInterval(LocalDateTime startDate, LocalDateTime endDate, String interval) {
        Interval intervalVal;
        try{
            intervalVal = Interval.valueOf(interval);
        }catch (IllegalArgumentException e){
            //standard value if invalid input
            intervalVal = Interval.HOUR;
        }
        List<AveragedMeasurement> measurements = averageMeasurementRepository.getAveragedMeasurementsByDeviceIdAndStartAndEndTimeAndTimeInterval(
                "1", startDate, endDate, intervalVal
        );
        return measurements.stream().map(DTOMapper::mapAverageMeasurmentToAverageMeasurementDTO).toList();
    }

    @Override
    public List<AccumulatedMeasurementsDTO> getAccumulatedMeasurementsBetweenDatesAndInterval(LocalDateTime startDate, LocalDateTime endDate, String interval) {
        Interval intervalVal;
        try{
            intervalVal = Interval.valueOf(interval);
        }catch (IllegalArgumentException e){
            //standard value if invalid input
            intervalVal = Interval.HOUR;
        }
        List<AccumulatedMeasurements> measurements = accumulatedMeasurementRepository.getAccumulatedMeasurementsBetweenDates(
                "1", startDate, endDate, intervalVal
        );
        return measurements.stream().map(DTOMapper::mapAccumulatedMeasurementToAccumulatedMeasurementDTO).toList();

    }

    @Override
    public List<MeasurementDTO> getMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        //todo: retrieve deviceId from session (?)
        return  measurementRepository
                .getMeasurementsByDeviceIdAndStartAndEndTime("1", startDate, endDate)
                .stream()
                .map(DTOMapper::mapMeasurementToMeasurementDTO)
                .toList();
    }

    @Override
    public List<String> getAllTagNames() {
        //todo: retrieve deviceId from session
        return measurementRepository.getAllLabelNamesByDeviceId("1");
    }
}
