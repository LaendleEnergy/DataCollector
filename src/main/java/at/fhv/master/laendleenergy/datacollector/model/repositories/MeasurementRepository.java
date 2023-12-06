package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.controller.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository {

    List<AveragedMeasurement> getNAveragedMeasurementsByDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime,
                                                                                   LocalDateTime endTime, int numberofGroups);
    Optional<Measurement> getMeasurementByDeviceIdAndTimeStamp();
    void saveMeasurement(Measurement measurement);
    void createMeasurementTable();
    void createUniqueIndexOnMeasurementTable();
    void convertMeasurementTableToHyperTable();
    void saveChanges(Measurement measurement);
    List<Measurement> getMeasurementsByDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime, LocalDateTime endTime);

}
