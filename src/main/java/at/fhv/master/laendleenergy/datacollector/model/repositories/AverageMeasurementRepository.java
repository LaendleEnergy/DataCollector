package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;

import java.time.LocalDateTime;
import java.util.List;

public interface AverageMeasurementRepository {
    List<AveragedMeasurement> getNAveragedMeasurementsByDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime,
                                                                                   LocalDateTime endTime, int numberofGroups);
    List<AveragedMeasurement> getAveragedMeasurementsByDeviceIdAndStartAndEndTimeAndTimeInterval(String deviceId, LocalDateTime startTime,
                                                                                                 LocalDateTime endTime, Interval interval);
}
