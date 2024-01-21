package at.fhv.master.laendleenergy.datacollector.model.repositories;

import at.fhv.master.laendleenergy.datacollector.model.Device;

import java.util.Optional;

public interface DeviceRepository {
    void saveDevice(Device device);

    Optional<Device> getDeviceByMeterDeviceIdAndDeviceName(String deviceId, String caption);
}
