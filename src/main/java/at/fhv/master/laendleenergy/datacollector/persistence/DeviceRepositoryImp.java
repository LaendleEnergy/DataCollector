package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.model.Device;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;


@ApplicationScoped
public class DeviceRepositoryImp implements DeviceRepository {

    @Inject
    EntityManager eM;

    @Override
    public void saveDevice(Device device) {
        eM.persist(device);
    }

    @Override
    public Optional<Device> getDeviceByMeterDeviceIdAndDeviceName(String deviceId, String deviceName) {
        return eM.createQuery("FROM Device " +
                "WHERE meterDeviceId = :meterDeviceId " +
                "AND name = :deviceName", Device.class)
                .setParameter("deviceName", deviceName)
                .setParameter("meterDeviceId", deviceId)
                .getResultStream().findFirst();
    }
}
