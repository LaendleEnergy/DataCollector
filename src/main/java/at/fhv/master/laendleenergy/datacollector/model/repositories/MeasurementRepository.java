package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.model.Measurement;

import java.util.Optional;

public interface MeasurementRepository {
    Optional<Measurement> getMeasurementByDeviceIdAndTimeStamp();
    void saveMeasurement(Measurement measurement);
}
