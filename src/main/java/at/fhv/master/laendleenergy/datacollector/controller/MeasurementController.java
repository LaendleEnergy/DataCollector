package at.fhv.master.laendleenergy.datacollector.controller;


import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    @Inject
    MeasurementService measurementService;


    @GetMapping("/tags/names/all")
    public RestResponse<List<String>> getTagNames(){
        return RestResponse.ok(measurementService.getAllTagNames());
    }

    @GetMapping("/")
    public RestResponse<List<MeasurementDTO>> getMeasurementsBetweenDates(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate){
        return RestResponse.ok(
                measurementService.getMeasurementsBetweenDates(startDate, endDate)
        );
    }

    @GetMapping("/averaged/")
    public RestResponse<List<AverageMeasurementDTO>> getMeasurementsBetweenDates(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("numberOfGroups") Integer numberOfGroups){
        return RestResponse.ok(
                measurementService.getAveragedMeasurementsBetweenDates(startDate, endDate, numberOfGroups)
        );
    }

    @PostMapping("/tags/")
    public RestResponse<String> addTagToMeasurements(TagDto tag){
        try {
            measurementService.addTag(tag.startTime, tag.endTime, tag.caption, tag.deviceCategoryName);

        } catch (DeviceCategoryNotFoundException | MeasurementNotFoundException e) {
            //todo: fix no string response
            return RestResponse.notFound();
        }
        return RestResponse.ok("Tag successfully added");
    }


}
