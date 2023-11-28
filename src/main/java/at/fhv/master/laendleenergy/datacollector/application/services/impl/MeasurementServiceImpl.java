package at.fhv.master.laendleenergy.datacollector.application.services.impl;

import at.fhv.master.laendleenergy.datacollector.application.services.DTOMapper;
import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.controller.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import at.fhv.master.laendleenergy.datacollector.model.Tag;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceCategoryRepository;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
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
    DeviceCategoryRepository deviceCategoryRepository;

    @Override
    @Transactional
    public void addTag(LocalDateTime startTime, LocalDateTime endTime, String deviceId,
                       String caption, String deviceCategoryName) throws MeasurementNotFoundException, DeviceCategoryNotFoundException {
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
        }
    }

    @Override
    public List<MeasurementDTO> getMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        //todo: retrieve deviceId from session (?)
        List<Measurement> measurements = measurementRepository.getMeasurementsByDeviceIdAndStartAndEndTime(
                "1", startDate, endDate
        );
        return measurements.stream().map(DTOMapper::mapMeasurementToMeasurementDTO).toList();
    }
}
