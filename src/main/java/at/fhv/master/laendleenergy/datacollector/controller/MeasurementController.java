package at.fhv.master.laendleenergy.datacollector.controller;


import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.jboss.resteasy.reactive.RestResponse.ResponseBuilder.notFound;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    @Inject
    MeasurementService measurementService;

    @PostMapping("/tags/")
    public RestResponse<String> addTagToMeasurements(TagDto tag){
        try {
            measurementService.addTag(tag.startTime, tag.endTime,
                   tag.deviceId, tag.caption, tag.deviceCategoryName);

        } catch (DeviceCategoryNotFoundException | MeasurementNotFoundException e) {
            //todo: fix no string response
            return RestResponse.notFound();
        }
        return RestResponse.ok("Tag successfully added");
    }
}
