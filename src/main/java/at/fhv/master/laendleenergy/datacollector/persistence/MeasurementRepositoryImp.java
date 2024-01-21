package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
                "PRIMARY KEY (time, device_id)" +
                ");").executeUpdate();

    }

    @Transactional
    public void createUniqueIndexOnMeasurementTable(){
        eM.createNativeQuery("CREATE UNIQUE INDEX idx_timestamp_device_id " +
                "  ON measurement(device_id, reading_time);").executeUpdate();
    }


    @Transactional
    public void convertMeasurementTableToHyperTable(){
        List<Object> result = eM.createNativeQuery("SELECT create_hypertable('measurement', 'time',  partitioning_column => 'device_id', " +
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
    public List<String> getAllLabelNamesByDeviceId(String deviceId) {
        return eM.createNativeQuery(" SELECT DISTINCT device_name" +
                " FROM tag " +
                        "WHERE measurement_device_id = :deviceId"
        )
                .setParameter("deviceId", deviceId)
                .getResultList()
                .stream()
                .toList();
    }

    public List<AveragedMeasurement> getAveragedMeasurementsByDeviceIdAndStartAndEndTimeAndInterval(String deviceId,
                                                                                                    LocalDateTime startTime,
                                                                                                    LocalDateTime endTime,
                                                                                                    int numberOfGroups,
                                                                                                    Interval interval
    ){
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
                                "MIN(reading_time) as t_start, MAX(reading_time) as t_end, " +
                                "time_bucket(((SELECT MAX(reading_time) FROM measurement where reading_time <= :endTime) " +
                                " - (SELECT MIN(reading_time) FROM measurement where reading_time >= :startTime)) / :numberOfGroups, reading_time) as timestamp_start " +
                                "FROM measurement " +
                                "WHERE reading_time <= :endTime " +
                                " and reading_time >= :startTime  " +
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

    public List<AveragedMeasurement> getNAveragedMeasurementsByDeviceIdAndStartAndEndTime(String deviceId,
                                                                                          LocalDateTime startTime,
                                                                                          LocalDateTime endTime, int numberOfGroups){
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
                        "MIN(reading_time) as t_start, MAX(reading_time) as t_end, " +
                        "time_bucket(((SELECT MAX(reading_time) FROM measurement where reading_time <= :endTime) " +
        " - (SELECT MIN(reading_time) FROM measurement where reading_time >= :startTime)) / :numberOfGroups, reading_time) as timestamp_start " +
                "FROM measurement " +
                "WHERE reading_time <= :endTime " +
                " and reading_time >= :startTime  " +
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
        eM.flush();
    }
}
