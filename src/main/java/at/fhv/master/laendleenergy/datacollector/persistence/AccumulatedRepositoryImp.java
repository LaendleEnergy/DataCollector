package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.repositories.AccumulatedMeasurementRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class AccumulatedRepositoryImp implements AccumulatedMeasurementRepository {

    @Inject
    EntityManager eM;

    @Override
    public List<AccumulatedMeasurements> getAccumulatedMeasurementsBetweenDates(String deviceId, LocalDateTime startTime, LocalDateTime endTime, Interval interval) {
        List<AccumulatedMeasurements> accumulatedMeasurements = eM.createNativeQuery(
                        "WITH measurements_accumuluated AS (SELECT time_bucket(CAST(:interval AS INTERVAL), reading_time)            AS week_start, " +
                                "                                          last(total_energy_consumed_wh, reading_time) - " +
                                "                                          first(total_energy_consumed_wh, reading_time)  AS energy_consumed_wh, " +
                                "                                          last(total_energy_delivered_wh, reading_time) - " +
                                "                                          first(total_energy_delivered_wh, reading_time) AS energy_delivered_wh " +
                                "                                   FROM measurement_w_t " +
                                "                                   WHERE device_id = :deviceId " +
                                "                                   AND reading_time <= :endTime " +
                                "                                   AND reading_time >= :startTime  " +
                                "                                   GROUP BY week_start " +
                                "                                   ORDER BY week_start) " +
                                "SELECT *, ROUND(CAST((SELECT DISTINCT ON(device_id) \"average_price_Wh\" " +
                                "            FROM averagepriceperkwh " +
                                "            WHERE averagepriceperkwh.device_id = :deviceId " +
                                "              AND  averagepriceperkwh.start_date <= week_start " +
                                "            ORDER BY device_id, averagepriceperkwh.start_date) AS numeric) * energy_consumed_wh) / 100 AS energy_consumed_price_euro " +
                                "FROM measurements_accumuluated")
                .setParameter("interval", "1 " + interval.toString())
                .setParameter("endTime", endTime)
                .setParameter("startTime", startTime)
                .setParameter("deviceId", "1")
                .getResultList();

        return accumulatedMeasurements;
    }

}
