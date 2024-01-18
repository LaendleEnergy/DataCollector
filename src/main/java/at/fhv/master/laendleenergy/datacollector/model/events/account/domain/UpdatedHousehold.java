package at.fhv.master.laendleenergy.datacollector.model.events.account;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatedHouseholdEvent {

    @JsonProperty
    private String id;
    @JsonProperty
    private ElectricityPricingPlan pricingPlan;
    @JsonProperty
    private String deviceId;
}
