package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;

import java.util.List;

public interface DeviceCategoryService {
    List<DeviceCategoryDTO> getAllDeviceCategories();
}
