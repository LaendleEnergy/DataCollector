package at.fhv.master.laendleenergy.datacollector.controller;


import at.fhv.master.laendleenergy.datacollector.application.services.DeviceCategoryService;
import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryAlreadyExistsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deviceCategories")
public class DeviceCategoryController {

    @Inject
    DeviceCategoryService deviceCategoryService;

    @GetMapping("/all")
    @Operation(summary = "Get all available device categories",
            description = "Returns all available device categories.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200", description = "Device Categories"),
            @APIResponse(
                    responseCode = "401", description = "Unauthorized"),
            @APIResponse(
                    responseCode = "500", description = "Server Error")}
    )
    @PermitAll
    public RestResponse getAllDeviceCategories(){
        return RestResponse.ok(
               deviceCategoryService.getAllDeviceCategories()
        );
    }


    //in a later version addDeviceCategory should only be accessed by a system administrator
    @POST
    @Operation(summary = "Adds device category",
            description = "Returns success of added device category.\n" +
                    "Method can only be accessed by sys-admin (not implemented).")
    @APIResponses({
            @APIResponse(
                    responseCode = "200", description = "Device Categories"),
            @APIResponse(
                    responseCode = "401", description = "Unauthorized"),
            @APIResponse(
                    responseCode = "403", description = "Device Category already exists"),
            @APIResponse(
                    responseCode = "500", description = "Server Error")}
    )
    @PermitAll
    public RestResponse addDeviceCategory(DeviceCategoryDTO deviceCategoryDTO){
        try {
            deviceCategoryService.addDeviceCategory(deviceCategoryDTO);
            return RestResponse.ok("DeviceCategory successfully added");
        } catch (JsonProcessingException e) {
            return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR);
        } catch (DeviceCategoryAlreadyExistsException e) {
            return RestResponse.status(403, e.toString());
        }

    }
}
