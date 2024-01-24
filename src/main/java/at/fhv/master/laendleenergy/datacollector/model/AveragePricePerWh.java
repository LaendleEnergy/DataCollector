package at.fhv.master.laendleenergy.datacollector.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "averagepriceperwh")
public class AveragePricePerWh {
    @Id
    @Column(name = "meter_device_id")
    String meterDeviceId;
    @Id
    @Column(name = "start_date")
    LocalDateTime startDate;
    @Column(name = "average_price_Wh")
    float averagePriceWh;

    public String getMeterDeviceId() {
        return meterDeviceId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public float getAveragePriceWh() {
        return averagePriceWh;
    }

    public AveragePricePerWh() {
    }

    public AveragePricePerWh(String meterDeviceId, LocalDateTime startDate, float averagePriceWh) {
        this.meterDeviceId = meterDeviceId;
        this.startDate = startDate;
        this.averagePriceWh = averagePriceWh;
    }
}
