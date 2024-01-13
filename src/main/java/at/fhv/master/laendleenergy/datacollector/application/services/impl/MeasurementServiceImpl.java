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
import org.eclipse.microprofile.jwt.JsonWebToken;

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
    JsonWebToken jwt;

    @Inject
    DeviceCategoryRepository deviceCategoryRepository;

    @Override
    @Transactional
    public void addTag(LocalDateTime startTime, LocalDateTime endTime,
                       String caption, String deviceCategoryName) throws MeasurementNotFoundException, DeviceCategoryNotFoundException, JsonProcessingException {


        String deviceId = jwt.getClaim("deviceId");
        String userId = jwt.getClaim("memberId");
        String householdId = jwt.getClaim("householdId");

        List<Measurement> measurements = measurementRepository.getMeasurementsByDeviceIdAndStartAndEndTime(deviceId, startTime, endTime);

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
                    userId, deviceId, householdId
            ));
        }
    }

    @Override
    public List<AverageMeasurementDTO> getAveragedMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, int numberOfGroups) {
        String deviceId = jwt.getClaim("deviceId");
        List<AveragedMeasurement> measurements = averageMeasurementRepository.getNAveragedMeasurementsByDeviceIdAndStartAndEndTime(
                deviceId, startDate, endDate, numberOfGroups
        );
        return measurements.stream().map(DTOMapper::mapAverageMeasurmentToAverageMeasurementDTO).toList();
    }

    @Override
    public List<AverageMeasurementDTO> getAveragedMeasurementsBetweenDatesAndInterval(LocalDateTime startDate, LocalDateTime endDate, String interval) {
        Interval intervalVal;
        try{
            intervalVal = Interval.valueOf(interval.toUpperCase());
        }catch (IllegalArgumentException e){
            //standard value if invalid input
            intervalVal = Interval.HOUR;
        }
        String deviceId = jwt.getClaim("deviceId");
        List<AveragedMeasurement> measurements = averageMeasurementRepository.getAveragedMeasurementsByDeviceIdAndStartAndEndTimeAndTimeInterval(
                deviceId, startDate, endDate, intervalVal
        );
        return measurements.stream().map(DTOMapper::mapAverageMeasurmentToAverageMeasurementDTO).toList();
    }

    @Override
    public List<AccumulatedMeasurementsDTO> getAccumulatedMeasurementsBetweenDatesAndInterval(LocalDateTime startDate, LocalDateTime endDate, String interval) {
        Interval intervalVal;
        try{
            intervalVal = Interval.valueOf(interval.toUpperCase());
        }catch (IllegalArgumentException e){
            //standard value if invalid input
            intervalVal = Interval.HOUR;
        }
        String deviceId = jwt.getClaim("deviceId");
        List<AccumulatedMeasurements> measurements = accumulatedMeasurementRepository.getAccumulatedMeasurementsBetweenDates(
                deviceId, startDate, endDate, intervalVal
        );
        return measurements.stream().map(DTOMapper::mapAccumulatedMeasurementToAccumulatedMeasurementDTO).toList();

    }

    @Override
    public List<MeasurementDTO> getMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        String deviceId = jwt.getClaim("deviceId");
        return  measurementRepository
                .getMeasurementsByDeviceIdAndStartAndEndTime(deviceId, startDate, endDate)
                .stream()
                .map(DTOMapper::mapMeasurementToMeasurementDTO)
                .toList();
    }

    @Override
    public List<String> getAllTagNames() {
        String deviceId = jwt.getClaim("deviceId");
        return measurementRepository.getAllLabelNamesByDeviceId(deviceId);
    }
}
