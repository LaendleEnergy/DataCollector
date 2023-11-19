package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.postgresql.core.NativeQuery;

import java.time.LocalDateTime;
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
                "timestamp TIMESTAMPTZ NOT NULL," +
                "device_id varchar(255), " +
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
                "PRIMARY KEY (timestamp, device_id)" +
                ");").executeUpdate();

    }

    @Transactional
    public void createUniqueIndexOnMeasurementTable(){
        eM.createNativeQuery("CREATE UNIQUE INDEX idx_timestamp_device_id " +
                "  ON measurement(device_id, timestamp);").executeUpdate();
    }


    @Transactional
    public void convertMeasurementTableToHyperTable(){
        List<Object> result = eM.createNativeQuery("SELECT create_hypertable('measurement', 'timestamp',  partitioning_column => 'device_id', " +
                "  number_partitions => 10);").getResultList();
    }

    @Transactional
    public void saveMeasurement(Measurement measurement){
        Query query = eM.createNativeQuery(
                "INSERT INTO measurement (device_id, timestamp, current_l1a, current_l2a, current_l3a," +
                        " voltage_l1v, voltage_l2v, voltage_l3v, instantaneous_active_power_plus_w, instantaneous_active_power_minus_w," +
                        " total_energy_consumed_wh, total_energy_delivered_wh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        query.setParameter(1, measurement.getDeviceId());
        query.setParameter(2, measurement.getTimestamp());
        query.setParameter(3, measurement.getCurrentL1A());
        query.setParameter(4, measurement.getCurrentL2A());
        query.setParameter(5, measurement.getCurrentL3A());
        query.setParameter(6, measurement.getVoltageL1V());
        query.setParameter(7, measurement.getVoltageL2V());
        query.setParameter(8, measurement.getVoltageL3V());
        query.setParameter(9, measurement.getInstantaneousActivePowerPlusW());
        query.setParameter(10, measurement.getInstantaneousActivePowerMinusW());
        query.setParameter(11, measurement.getTotalEnergyConsumedWh());
        query.setParameter(12, measurement.getTotalEnergyDeliveredWh());
        query.executeUpdate();
    }

    public List<Measurement> getMeasurementsByDeviceIdAndStartAndEndTime(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Measurement> measurements = eM.createQuery("FROM Measurement" +
                        " WHERE measurementId.timestamp >= :startTime AND measurementId.timestamp <= :endTime" +
                        " AND measurementId.deviceId = :deviceId", Measurement.class)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setParameter("deviceId", deviceId)
                .getResultList();
        return measurements;
    }


    public void saveChanges(Measurement measurement){
        eM.persist(measurement);
    }
}
