package at.fhv.master.laendleenergy.datacollector.model.events.account.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatedHousehold {

    @JsonProperty
    private String id;
    @JsonProperty
    private ElectricityPricingPlan pricingPlan;
    @JsonProperty
    private String deviceId;

    public String getId() {
        return id;
    }

    public ElectricityPricingPlan getPricingPlan() {
        return pricingPlan;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
