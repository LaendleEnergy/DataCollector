package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.model.AveragePricePerWh;
import at.fhv.master.laendleenergy.datacollector.model.repositories.AveragePricePerWhRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class AveragePricePerWhRepositoryImp implements AveragePricePerWhRepository {
    @Inject
    EntityManager eM;

    @Override
    public Optional<AveragePricePerWh> findMostRecentAveragePricePerWhForGivenMeterDeviceId(String deviceId) {
        return eM.createQuery(
                "FROM AveragePricePerWh " +
                        "WHERE meterDeviceId = :meterDeviceId " +
                        "ORDER BY startDate DESC", AveragePricePerWh.class
        ).getResultList().stream().findFirst();
    }

    @Override
    public void saveAveragePricePerWh(AveragePricePerWh averagePricePerWh) {
        eM.persist(averagePricePerWh);
    }
}
