package at.fhv.master.laendleenergy.datacollector.application;


import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.application.streams.publisher.TaggingCreatedEventPublisher;
import at.fhv.master.laendleenergy.datacollector.model.*;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@QuarkusTest
public class MeasurementServiceTests {


    @Inject
    MeasurementService measurementService;

    @InjectMock
    MeasurementRepository measurementRepository;

    @InjectMock
    AverageMeasurementRepository averageMeasurementRepository;

    @InjectMock
    AccumulatedMeasurementRepository accumulatedMeasurementRepository;

    @InjectMock
    TaggingCreatedEventPublisher taggingCreatedEventPublisher;

    @InjectMock
    JsonWebToken jwt;

    @InjectMock
    TagRepository tagRepository;


    @InjectMock
    DeviceRepository deviceRepository;

    @InjectMock
    DeviceCategoryRepository deviceCategoryRepository;



    //reoccurring data
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusDays(1);
    DeviceCategory deviceCategory1 = new DeviceCategory("Waschmaschine");
    DeviceCategory deviceCategory2 = new DeviceCategory("Toaster");
    Device device = new Device("Waschmaschine 1", "D1", deviceCategory1);
    Measurement measurement = new Measurement("D1", LocalDateTime.now(), 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0);
    AveragedMeasurement averagedMeasurement = new AveragedMeasurement(LocalDateTime.now(),  LocalDateTime.now().plusHours(1), "D1",0, 0,
            0, 0, 0, 0, 0, 0);

    AccumulatedMeasurements accumulatedMeasurement = new AccumulatedMeasurements(LocalDateTime.now(),  LocalDateTime.now().plusHours(1), "D1",
            0, 0, 0);



    @BeforeEach
    void setUp() {
        Mockito.when(deviceRepository.getDeviceByMeterDeviceIdAndDeviceName(any(), any())).thenReturn(Optional.of(device));
        Mockito.when(deviceCategoryRepository.getAllDeviceCategories()).thenReturn(List.of(deviceCategory1, deviceCategory2));
        Mockito.when(deviceCategoryRepository.getDeviceCategoryByName(deviceCategory1.getCategoryName())).thenReturn(Optional.of(deviceCategory1));
        Mockito.when(jwt.getClaim("deviceId")).thenReturn("D1");
        Mockito.when(jwt.getClaim("householdId")).thenReturn("H1");
        Mockito.when(jwt.getClaim("memberId")).thenReturn("M1");
    }

    @Test
    void addTagTest() throws DeviceNotFoundException, DeviceCategoryNotFoundException, MeasurementNotFoundException, JsonProcessingException {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);
        String deviceName = "Waschmaschine 1";
        String deviceCategoryName = "Waschmaschine";

        Mockito.when(measurementRepository.getMeasurementsByDeviceIdAndStartAndEndTime(any(), any(), any()))
                .thenReturn(List.of(measurement));


        measurementService.addTag(startTime, endTime, deviceName, deviceCategoryName);

        Mockito.verify(deviceCategoryRepository, times(1)).getDeviceCategoryByName(any());
        Mockito.verify(deviceRepository, times(1)).getDeviceByMeterDeviceIdAndDeviceName(any(), any());
        Mockito.verify(tagRepository, times(1)).saveTag(any());
        Mockito.verify(taggingCreatedEventPublisher, times(1)).publishMessage(any());
    }


    @Test
    void getAveragedMeasurementsBetweenDatesTest(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);

        Mockito.when(averageMeasurementRepository.getNAveragedMeasurementsByDeviceIdAndStartAndEndTime("D1", startTime, endTime, 100)).thenReturn(
                List.of(averagedMeasurement)
        );

        measurementService.getAveragedMeasurementsBetweenDates(startTime, endTime, 100);

        Mockito.verify(averageMeasurementRepository, times(1))
                .getNAveragedMeasurementsByDeviceIdAndStartAndEndTime("D1", startTime, endTime, 100);
    }


    @Test
    void getAveragedMeasurementsBetweenDatesAndIntervalTest(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);
        Interval interval = Interval.DAY;

        Mockito.when(averageMeasurementRepository.getAveragedMeasurementsByDeviceIdAndStartAndEndTimeAndTimeInterval("D1", startTime, endTime, Interval.DAY)).thenReturn(
                List.of(averagedMeasurement)
        );

        measurementService.getAveragedMeasurementsBetweenDatesAndInterval(startTime, endTime, interval.toString());

        Mockito.verify(averageMeasurementRepository, times(1))
                .getAveragedMeasurementsByDeviceIdAndStartAndEndTimeAndTimeInterval("D1", startTime, endTime,  interval);

    }

    @Test
    void getAccumulatedMeasurementsBetweenDatesAndIntervalTest(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);
        Interval interval = Interval.DAY;

        Mockito.when(accumulatedMeasurementRepository.getAccumulatedMeasurementsBetweenDates("D1", startTime, endTime, Interval.DAY))
                .thenReturn(
                List.of(accumulatedMeasurement)
        );

        measurementService.getAccumulatedMeasurementsBetweenDatesAndInterval(startTime, endTime, interval.toString());

        Mockito.verify(accumulatedMeasurementRepository, times(1))
                .getAccumulatedMeasurementsBetweenDates("D1", startTime, endTime,  interval);

    }


    @Test
    void getMeasurementsBetweenDatesTest(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);

        Mockito.when(measurementRepository.getMeasurementsByDeviceIdAndStartAndEndTime("D1", startTime, endTime)).thenReturn(
                List.of(measurement)
        );

        measurementService.getMeasurementsBetweenDates(startTime, endTime);

        Mockito.verify(measurementRepository, times(1))
                .getMeasurementsByDeviceIdAndStartAndEndTime("D1", startTime, endTime);

    }


    @Test
    void  getAllTagNamesTest(){
        Mockito.when(measurementRepository.getAllLabelNamesByDeviceId("D1")).thenReturn(
                List.of("Waschmaschine 1")
        );
        measurementService.getAllTagNames();
        Mockito.verify(measurementRepository, times(1))
                .getAllLabelNamesByDeviceId("D1");

    }

}
