package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.model.Measurement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository {
    Optional<Measurement> getMeasurementByDeviceIdAndTimeStamp();
    void saveMeasurement(Measurement measurement);
    void createMeasurementTable();
    void createUniqueIndexOnMeasurementTable();
    void convertMeasurementTableToHyperTable();

    List<Measurement> getMeasurementsByDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime, LocalDateTime endTime);
}
