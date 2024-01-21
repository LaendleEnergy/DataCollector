package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.model.AveragePricePerWh;

import java.util.Optional;

public interface AveragePricePerWhRepository {
    Optional<AveragePricePerWh> findMostRecentAveragePricePerWhForGivenMeterDeviceId(String deviceId);

    void saveAveragePricePerWh(AveragePricePerWh averagePricePerWh);
}
