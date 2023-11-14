package at.fhv.master.laendleenergy.datacollector.application;


import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class StartupService {

    @Inject
    MeasurementRepository measurementRepository;

    void initTable(@Observes StartupEvent event) {
        measurementRepository.createMeasurementTable();
        measurementRepository.createUniqueIndexOnMeasurementTable();
        measurementRepository.convertMeasurementTableToHyperTable();
    }

}
