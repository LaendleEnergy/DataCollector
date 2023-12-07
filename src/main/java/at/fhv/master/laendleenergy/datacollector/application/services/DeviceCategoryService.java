package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.DeviceCategoryDTO;

import java.util.List;

public interface DeviceCategoryService {
    List<DeviceCategoryDTO> getAllDeviceCategories();
}
