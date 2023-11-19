package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;

import java.util.Optional;

public interface DeviceCategoryRepository {
    Optional<DeviceCategory> getDeviceCategoryByName(String name);
}
