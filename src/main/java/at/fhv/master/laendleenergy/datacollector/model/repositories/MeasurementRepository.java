package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.model.Measurement;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementRepository {
    List<String> getAllLabelNamesByDeviceId(String deviceId);
    void saveMeasurement(Measurement measurement);
    void saveChanges(Measurement measurement);
    List<Measurement> getMeasurementsByMeterDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime, LocalDateTime endTime);

}
