package at.fhv.master.laendleenergy.datacollector.model.events.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElectricityPricingPlan {
    @JsonProperty
    private double averagePricePerKwh;
}
