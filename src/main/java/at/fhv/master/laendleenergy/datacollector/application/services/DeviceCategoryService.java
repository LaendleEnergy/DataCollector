package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryAlreadyExistsException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DeviceCategoryService {
    List<DeviceCategoryDTO> getAllDeviceCategories();

    void addDeviceCategory(DeviceCategoryDTO category) throws JsonProcessingException, DeviceCategoryAlreadyExistsException;
}
