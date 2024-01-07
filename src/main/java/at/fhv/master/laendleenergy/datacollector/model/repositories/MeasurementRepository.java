package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository {

    List<String> getAllLabelNamesByDeviceId(String deviceId);


    Optional<Measurement> getMeasurementByDeviceIdAndTimeStamp();
    void saveMeasurement(Measurement measurement);
    void createMeasurementTable();
    void createUniqueIndexOnMeasurementTable();
    void convertMeasurementTableToHyperTable();
    void saveChanges(Measurement measurement);
    List<Measurement> getMeasurementsByDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime, LocalDateTime endTime);

}
