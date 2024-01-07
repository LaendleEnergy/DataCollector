package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;

import java.time.LocalDateTime;
import java.util.List;

public interface AccumulatedMeasurementRepository {
    List<AccumulatedMeasurements> getAccumulatedMeasurementsBetweenDates(String deviceId, LocalDateTime startTime, LocalDateTime endTime, Interval interval);
}
