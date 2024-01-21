package at.fhv.master.laendleenergy.datacollector.controller;


import at.fhv.master.laendleenergy.datacollector.application.services.DeviceCategoryService;
import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deviceCategories")
public class DeviceCategoryController {

    @Inject
    DeviceCategoryService deviceCategoryService;

    @GetMapping("/all")
    @PermitAll
    public RestResponse<List<DeviceCategoryDTO>> getAllDeviceCategories(){
        return RestResponse.ok(
               deviceCategoryService.getAllDeviceCategories()
        );
    }


    //in a later version addDeviceCategory should only be accessed by a system administrator
    @POST
    @PermitAll
    public RestResponse<String> addDeviceCategory(DeviceCategoryDTO deviceCategoryDTO){
        try {
            deviceCategoryService.addDeviceCategory(deviceCategoryDTO);
            return RestResponse.ok("DeviceCategory successfully added");
        } catch (JsonProcessingException e) {
            return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR);
        }

    }
}
