package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.model.Device;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

public class DeviceRepositoryImp implements DeviceRepository {

    @Inject
    EntityManager eM;

    @Override
    public void saveDevice(Device device) {
        eM.persist(device);
    }
}
