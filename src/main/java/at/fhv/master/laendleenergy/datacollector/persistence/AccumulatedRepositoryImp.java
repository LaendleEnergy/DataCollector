package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.repositories.AccumulatedMeasurementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@ApplicationScoped
public class AccumulatedRepositoryImp implements AccumulatedMeasurementRepository {

    @Inject
    EntityManager eM;

    @Override
    public List<AccumulatedMeasurements> getAccumulatedMeasurementsBetweenDates(String meterDeviceId, LocalDateTime startTime, LocalDateTime endTime, Interval interval) {
        List<Object[]> accumulatedMeasurements = eM.createNativeQuery(
                        "WITH measurements_accumuluated AS (SELECT time_bucket(CAST(:interval AS INTERVAL), reading_time)            AS time_start, " +
                                "                                          max(reading_time) as time_end, " +
                                "                                          last(total_energy_consumed_wh, reading_time) - " +
                                "                                          first(total_energy_consumed_wh, reading_time)  AS energy_consumed_wh, " +
                                "                                          last(total_energy_delivered_wh, reading_time) - " +
                                "                                          first(total_energy_delivered_wh, reading_time) AS energy_delivered_wh " +
                                "                                   FROM measurement_w_t " +
                                "                                   WHERE meter_device_id = :meterDeviceId " +
                                "                                   AND reading_time <= :endTime " +
                                "                                   AND reading_time >= :startTime  " +
                                "                                   GROUP BY time_start " +
                                "                                   ORDER BY time_start) " +
                                "SELECT *, CAST(ROUND(CAST((SELECT DISTINCT ON(meter_device_id) \"average_price_wh\" " +
                                "            FROM averagepriceperwh " +
                                "            WHERE averagepriceperwh.meter_device_id = :meterDeviceId " +
                                "              AND  averagepriceperwh.start_date <= time_start " +
                                "            ORDER BY meter_device_id, averagepriceperwh.start_date) AS numeric) * energy_consumed_wh) / 100 AS float4) AS energy_consumed_price_euro " +
                                "FROM measurements_accumuluated")
                .setParameter("interval",  interval.toString())
                .setParameter("endTime", endTime)
                .setParameter("startTime", startTime)
                .setParameter("meterDeviceId", meterDeviceId)
                .getResultList();

        return accumulatedMeasurements.stream().map(
                measurement -> {return new AccumulatedMeasurements(
                        LocalDateTime.ofInstant(((Timestamp) measurement[0]).toInstant(), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(((Timestamp) measurement[1]).toInstant(), ZoneId.systemDefault()),
                        meterDeviceId,
                        (float) measurement[2],
                        (float) measurement[3],
                        (float) measurement[4]
                );}
        ).toList();
    }

}
