package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceCategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DeviceCategoryRepositoryImp implements DeviceCategoryRepository {

    @Inject
    EntityManager eM;

    @Override
    public Optional<DeviceCategory> getDeviceCategoryByName(String name) {
        return eM.createQuery("FROM DeviceCategory WHERE categoryName = :categoryName", DeviceCategory.class)
                .setParameter("categoryName", name).getResultList().stream().findFirst();
    }

    @Override
    public List<DeviceCategory> getAllDeviceCategories() {
        return eM.createQuery("FROM DeviceCategory ", DeviceCategory.class)
                .getResultList();
    }
}
