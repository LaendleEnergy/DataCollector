package at.fhv.master.laendleenergy.datacollector.application.services.impl;

import at.fhv.master.laendleenergy.datacollector.application.services.DTOMapper;
import at.fhv.master.laendleenergy.datacollector.application.services.DeviceCategoryService;
import at.fhv.master.laendleenergy.datacollector.controller.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceCategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;


@ApplicationScoped
public class DeviceCategoryServiceImpl implements DeviceCategoryService {

    @Inject
    DeviceCategoryRepository deviceCategoryRepository;

    @Override
    public List<DeviceCategoryDTO> getAllDeviceCategories() {
        return deviceCategoryRepository.getAllDeviceCategories()
                .stream().map(DTOMapper::mapDeviceCategoryToDeviceCategoryDTO).toList();
    }
}
