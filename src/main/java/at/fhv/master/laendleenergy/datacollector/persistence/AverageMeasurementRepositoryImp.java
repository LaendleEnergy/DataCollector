package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.repositories.AverageMeasurementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import javax.swing.text.html.parser.Entity;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@ApplicationScoped
public class AverageMeasurementRepositoryImp implements AverageMeasurementRepository {

    @Inject
    EntityManager eM;

    @Override
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
            LocalDateTime timestampStart = LocalDateTime.ofInstant(((Timestamp) m[8]).toInstant(), ZoneId.systemDefault());
            LocalDateTime timestampEnd = LocalDateTime.ofInstant(((Timestamp) m[9]).toInstant(), ZoneId.systemDefault());
            return new AveragedMeasurement(timestampStart,timestampEnd, deviceId, currentL1A, currentL2A, currentL3A, voltageL1V, voltageL2V, voltageL3V,
                    instantaneousActivePowerPlusW, instantaneousActivePowerMinusW);
        }).toList();
    }

    @Override
    public List<AveragedMeasurement> getAveragedMeasurementsByDeviceIdAndStartAndEndTimeAndTimeInterval(String deviceId, LocalDateTime startTime, LocalDateTime endTime, Interval interval) {
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
                                "time_bucket(CAST(:interval AS INTERVAL), reading_time) as timestamp_start " +
                                "FROM measurement " +
                                "WHERE reading_time <= :endTime " +
                                " and reading_time >= :startTime  " +
                                " and device_id = :deviceId " +
                                "GROUP BY timestamp_start"
                )
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setParameter("deviceId", deviceId)
                .setParameter("interval", interval.toString())
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
            LocalDateTime timestampStart = LocalDateTime.ofInstant(((Timestamp) m[8]).toInstant(), ZoneId.systemDefault());
            LocalDateTime timestampEnd = LocalDateTime.ofInstant(((Timestamp) m[9]).toInstant(), ZoneId.systemDefault());
            return new AveragedMeasurement(timestampStart,timestampEnd, deviceId, currentL1A, currentL2A, currentL3A, voltageL1V, voltageL2V, voltageL3V,
                    instantaneousActivePowerPlusW, instantaneousActivePowerMinusW);
        }).toList();
    }
}
