package at.fhv.master.laendleenergy.datacollector.model.events.account;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatedHousehold {

    @JsonProperty
    private String id;
    @JsonProperty
    private ElectricityPricingPlan pricingPlan;
    @JsonProperty
    private String deviceId;
}
