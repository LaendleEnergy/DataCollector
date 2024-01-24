package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MeasurementRepositoryImp implements MeasurementRepository {

    @Inject
    private EntityManager eM;



    @Transactional
    public void saveMeasurement(Measurement measurement){
        try{
        eM.persist(measurement);}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Measurement> getMeasurementsByMeterDeviceIdAndStartAndEndTime(String meterDeviceId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Measurement> measurements = eM.createQuery("FROM Measurement " +
                        " WHERE measurementId.timestamp >= :startTime AND measurementId.timestamp <= :endTime" +
                        " AND measurementId.meterDeviceId = :meterDeviceId", Measurement.class)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setParameter("meterDeviceId", meterDeviceId)
                .getResultList();
        return measurements;
    }


    @Override
    public List<String> getAllLabelNamesByDeviceId(String meterDeviceId) {
        return eM.createNativeQuery(" SELECT DISTINCT device_name" +
                " FROM tag " +
                        "WHERE meter_device_id = :meterDeviceId"
        )
                .setParameter("meterDeviceId", meterDeviceId)
                .getResultList()
                .stream()
                .toList();
    }


    public void saveChanges(Measurement measurement){
        eM.merge(measurement);
        eM.flush();
    }
}
