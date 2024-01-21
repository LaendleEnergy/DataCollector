package at.fhv.master.laendleenergy.datacollector.model.events.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElectricityPricingPlan {
    @JsonProperty
    private float averagePricePerKwh;

    public float getAveragePricePerKwh() {
        return averagePricePerKwh;
    }
}
