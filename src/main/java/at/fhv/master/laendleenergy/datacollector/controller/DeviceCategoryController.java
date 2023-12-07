package at.fhv.master.laendleenergy.datacollector.controller;


import at.fhv.master.laendleenergy.datacollector.application.services.DeviceCategoryService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/deviceCategories")
public class DeviceCategoryController {

    @Inject
    DeviceCategoryService deviceCategoryService;

    @GetMapping("/all")
    public RestResponse<List<DeviceCategoryDTO>> getAllDeviceCategories(){
        return RestResponse.ok(
               deviceCategoryService.getAllDeviceCategories()
        );
    }
}
