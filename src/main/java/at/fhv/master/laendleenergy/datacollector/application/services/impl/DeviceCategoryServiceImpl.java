package at.fhv.master.laendleenergy.datacollector.application.services.impl;

import at.fhv.master.laendleenergy.datacollector.application.services.DTOMapper;
import at.fhv.master.laendleenergy.datacollector.application.services.DeviceCategoryService;
import at.fhv.master.laendleenergy.datacollector.application.streams.publisher.DeviceCategoryAddedEventPublisher;
import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceCategoryAddedEvent;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryAlreadyExistsException;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceCategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;


@ApplicationScoped
public class DeviceCategoryServiceImpl implements DeviceCategoryService {

    @Inject
    DeviceCategoryRepository deviceCategoryRepository;

    @Inject
    DeviceCategoryAddedEventPublisher deviceCategoryAddedEventPublisher;

    @Inject
    JsonWebToken jwt;

    @Override
    public List<DeviceCategoryDTO> getAllDeviceCategories() {
        return deviceCategoryRepository.getAllDeviceCategories()
                .stream().map(DTOMapper::mapDeviceCategoryToDeviceCategoryDTO).toList();
    }

    @Override
    @Transactional
    public void addDeviceCategory(DeviceCategoryDTO category) throws JsonProcessingException, DeviceCategoryAlreadyExistsException {
        String householdId = jwt.getClaim("householdId");
        String meterDeviceId = jwt.getClaim("deviceId");
        String memberId = jwt.getClaim("memberId");
        if(deviceCategoryRepository.getDeviceCategoryByName(category.getName()).isEmpty()) {
            DeviceCategory deviceCategory = new DeviceCategory(category.getName());
            deviceCategoryRepository.saveDeviceCategory(deviceCategory);
            deviceCategoryAddedEventPublisher.publishMessage(new DeviceCategoryAddedEvent(
                    category.getName(), meterDeviceId, householdId, memberId
            ));
        }
        else throw new DeviceCategoryAlreadyExistsException();
    }
}
