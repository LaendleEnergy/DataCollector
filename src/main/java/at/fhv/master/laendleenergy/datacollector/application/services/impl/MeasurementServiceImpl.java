package at.fhv.master.laendleenergy.datacollector.application.services.impl;

import at.fhv.master.laendleenergy.datacollector.application.services.DTOMapper;
import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.controller.dto.AverageMeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
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
    public void addTag(LocalDateTime startTime, LocalDateTime endTime,
                       String caption, String deviceCategoryName) throws MeasurementNotFoundException, DeviceCategoryNotFoundException {

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
        }
    }

    @Override
    public List<AverageMeasurementDTO> getAveragedMeasurementsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, int numberOfGroups) {
        //todo: retrieve deviceId from session (?)
        List<AveragedMeasurement> measurements = measurementRepository.getNAveragedMeasurementsByDeviceIdAndStartAndEndTime(
                "1", startDate, endDate, numberOfGroups
        );
        //return Collections.emptyList();
        return measurements.stream().map(DTOMapper::mapAverageMeasurmentToAverageMeasurementDTO).toList();
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
