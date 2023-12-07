package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import jakarta.ejb.Local;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.postgresql.core.NativeQuery;
import at.fhv.master.laendleenergy.datacollector.model.Tag;

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
                "  ON measurement(device_id, time);").executeUpdate();
    }


    @Transactional
    public void convertMeasurementTableToHyperTable(){
        List<Object> result = eM.createNativeQuery("SELECT create_hypertable('measurement', 'time',  partitioning_column => 'device_id', " +
                "  number_partitions => 10);").getResultList();
    }

    @Transactional
    public void saveMeasurement(Measurement measurement){
        Query query = eM.createNativeQuery(
                "INSERT INTO measurement (device_id, time, current_l1a, current_l2a, current_l3a," +
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
        /*for(Measurement measurement : measurements){
            //todo: fill up measurements
            List<Tag> tags = eM.createQuery("FROM Tag" +
                    " WHERE measurementTimestamp = :time " +
                    " AND measurementDeviceId = :deviceId ",
                    Tag.class)
                    .setParameter("time", measurement.getTimestamp())
                    .setParameter("deviceId", measurement.getDeviceId())
                    .getResultList();
            for(Tag tag : tags){
                measurement.addTag(tag);
            }
        }
        */
        return measurements;
    }

    @Override
    public List<String> getAllLabelNamesByDeviceId(String deviceId) {
        return eM.createNativeQuery(" SELECT DISTINCT name" +
                " FROM tag " +
                        "WHERE measurementDeviceId = :deviceId"
        )
                .setParameter("deviceId", deviceId)
                .getResultList()
                .stream()
                .toList();
    }

    public List<AveragedMeasurement> getNAveragedMeasurementsByDeviceIdAndStartAndEndTime(String deviceId,
                                                                                          LocalDateTime startTime,
                                                                                          LocalDateTime endTime,
                                                                                          int numberOfGroups){
        List<Object[]> measurements = eM.createNativeQuery(
                "SELECT " +
                        "avg(current_l1a) as avg_current_l1a, " +
                        "avg(current_l2a) as avg_current_l2a, " +
                        "avg(current_l3a) as avg_current_l3a, " +
                        "avg(voltage_l1v) as avg_voltage_l1v, " +
                        "avg(voltage_l2v) as avg_voltage_l2v, " +
                        "avg(voltage_l3v) as avg_voltage_l3v, " +
                        "avg(instantaneous_active_power_plus_w) as avg_instantaneous_active_power_plus_w, " +
                        "avg(instantaneous_active_power_minus_w) as avg_instantaneous_active_power_minus_w, " +
                        "MIN(timestamp) as t_start, MAX(timestamp) as t_end, " +
                        "time_bucket(((SELECT MAX(timestamp) FROM measurement where timestamp <= :endTime) " +
        " - (SELECT MIN(timestamp) FROM measurement where timestamp >= :startTime)) / :numberOfGroups, timestamp) as timestamp_start " +
                "FROM measurement " +
                "WHERE timestamp <= :endTime " +
                " and timestamp >= :startTime  " +
                " and device_id = :deviceId " +
                "GROUP BY timestamp_start"
        )
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setParameter("deviceId", deviceId)
                .setParameter("numberOfGroups", numberOfGroups - 1)
                .getResultList();



        return measurements.stream().map(m -> {
            float currentL1A = ((Double) m[0]).floatValue();
            float currentL2A = ((Double) m[1]).floatValue();
            float currentL3A = ((Double) m[2]).floatValue();
            float voltageL1V = ((Double) m[3]).floatValue();
            float voltageL2V = ((Double) m[4]).floatValue();
            float voltageL3V = ((Double) m[5]).floatValue();
            float instantaneousActivePowerPlusW = ((Double) m[6]).floatValue();
            float instantaneousActivePowerMinusW = ((Double) m[7]).floatValue();
            LocalDateTime timestampStart = LocalDateTime.ofInstant((Instant) m[8], ZoneId.systemDefault());
            LocalDateTime timestampEnd = LocalDateTime.ofInstant((Instant) m[9], ZoneId.systemDefault());
            return new AveragedMeasurement(timestampStart,timestampEnd, deviceId, currentL1A, currentL2A, currentL3A, voltageL1V, voltageL2V, voltageL3V,
                    instantaneousActivePowerPlusW, instantaneousActivePowerMinusW);
        }).toList();
    }


    public void saveChanges(Measurement measurement){
        eM.merge(measurement);
        for (at.fhv.master.laendleenergy.datacollector.model.Tag tag : measurement.getTags()){
            eM.merge(tag);
        }
    }
}
