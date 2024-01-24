package at.fhv.master.laendleenergy.datacollector.application.streams;


import at.fhv.master.laendleenergy.datacollector.model.AveragePricePerWh;
import at.fhv.master.laendleenergy.datacollector.model.Device;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.events.account.HouseholdUpdatedEvent;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceAddedEvent;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceCategoryAddedEvent;
import at.fhv.master.laendleenergy.datacollector.model.repositories.AveragePricePerWhRepository;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceCategoryRepository;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class EventHandler {
    @Inject
    DeviceCategoryRepository deviceCategoryRepository;
    @Inject
    DeviceRepository deviceRepository;
    @Inject
    AveragePricePerWhRepository averagePricePerWhRepository;

    @Transactional
    public void handleDeviceCategoryAddedEvent(DeviceCategoryAddedEvent event) {
        DeviceCategory deviceCategory = new DeviceCategory(event.getName());
        deviceCategoryRepository.saveDeviceCategory(deviceCategory);
    }


    @Transactional
    public void handleDeviceAddedEvent(DeviceAddedEvent deviceAddedEvent) {
        Optional<DeviceCategory> deviceCategoryOpt = deviceCategoryRepository.getDeviceCategoryByName(deviceAddedEvent.getDeviceCategoryName());
        Device device;
        if(deviceCategoryOpt.isPresent()) {
            device = new Device(deviceAddedEvent.getDeviceName(), deviceAddedEvent.getDeviceId(), deviceCategoryOpt.get());
        }
        else{
            DeviceCategory deviceCategory = new DeviceCategory(deviceAddedEvent.getDeviceCategoryName());
            deviceCategoryRepository.saveDeviceCategory(deviceCategory);
            device = new Device(deviceAddedEvent.getDeviceName(), deviceAddedEvent.getDeviceId(), deviceCategory);
        }
        deviceRepository.saveDevice(device);
    }

    @Transactional
    public void handleHouseholdUpdatedEvent(HouseholdUpdatedEvent householdUpdatedEvent) {
        String meterDeviceId = householdUpdatedEvent.getUpdatedHousehold().getMeterDeviceId();
        LocalDateTime updateTime = householdUpdatedEvent.getUpdateTime();
        float newAveragePricePerWh = householdUpdatedEvent.getUpdatedHousehold().getPricingPlan().getAveragePricePerKwh() / 1000;
        Optional<AveragePricePerWh> averagePricePerWhOpt =
                averagePricePerWhRepository.findMostRecentAveragePricePerWhForGivenMeterDeviceId(
                        householdUpdatedEvent.getUpdatedHousehold().getMeterDeviceId()
                );
        if(averagePricePerWhOpt.isPresent())
        {
            if(averagePricePerWhOpt.get().getAveragePriceWh() == newAveragePricePerWh){
                //no need for updating the Wh price
               return;
            }
        }
        averagePricePerWhRepository.saveAveragePricePerWh(new AveragePricePerWh(
                meterDeviceId, updateTime, newAveragePricePerWh
        ));

    }
}
