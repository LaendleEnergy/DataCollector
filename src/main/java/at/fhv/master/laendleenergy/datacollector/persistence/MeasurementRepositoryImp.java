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


    public Optional<Measurement> getMeasurementByDeviceIdAndTimeStamp(){
        return Optional.empty();
    }

    @Transactional
    public void createMeasurementTable(){
        eM.createNativeQuery("CREATE TABLE measurement(" +
                "time TIMESTAMPTZ NOT NULL," +
                "meter_device_id varchar(255), " +
                "current_l1a float, " +
                "current_l2a float, " +
                "current_l3a float, " +
                "voltage_l1v float, " +
                "voltage_l2v float, " +
                "voltage_l3v float, " +
                "instantaneous_active_power_plus_w float, " +
                "instantaneous_active_power_minus_w float, " +
                "total_energy_consumed_wh float, " +
                "total_energy_delivered_wh float, " +
                "PRIMARY KEY (time, meter_device_id)" +
                ");").executeUpdate();

    }

    @Transactional
    public void createUniqueIndexOnMeasurementTable(){
        eM.createNativeQuery("CREATE UNIQUE INDEX idx_timestamp_device_id " +
                "  ON measurement(meter_device_id, reading_time);").executeUpdate();
    }


    @Transactional
    public void convertMeasurementTableToHyperTable(){
        List<Object> result = eM.createNativeQuery("SELECT create_hypertable('measurement', 'time',  partitioning_column => 'meter_device_id', " +
                "  number_partitions => 10);").getResultList();
    }

    @Transactional
    public void saveMeasurement(Measurement measurement){
        try{
        eM.persist(measurement);}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Measurement> getMeasurementsByDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Measurement> measurements = eM.createQuery("FROM Measurement " +
                        " WHERE measurementId.timestamp >= :startTime AND measurementId.timestamp <= :endTime" +
                        " AND measurementId.meterDeviceId = :deviceId", Measurement.class)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setParameter("deviceId", deviceId)
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
