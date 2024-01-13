package at.fhv.master.laendleenergy.datacollector.controller;


import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.controller.dto.AccumulatedMeasurementsDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.AverageMeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.TagDTO;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
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
    @PermitAll
    public RestResponse<List<String>> getTagNames(){
        return RestResponse.ok(measurementService.getAllTagNames());
    }

    @GetMapping("/")
    @PermitAll
    public RestResponse<List<MeasurementDTO>> getMeasurementsBetweenDates(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate){
        return RestResponse.ok(
                measurementService.getMeasurementsBetweenDates(startDate, endDate)
        );
    }

    @GetMapping("/averaged/")
    @PermitAll
    public RestResponse<List<AverageMeasurementDTO>> getMeasurementsBetweenDates(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("numberOfGroups") Integer numberOfGroups, @RequestParam("interval") String interval){
        if(interval != null){
            return RestResponse.ok(
                    measurementService.getAveragedMeasurementsBetweenDatesAndInterval(startDate, endDate, interval)
            );
        }
        return RestResponse.ok(
                measurementService.getAveragedMeasurementsBetweenDates(startDate, endDate, numberOfGroups)
        );
    }


    @GetMapping("/accumulated")
    @PermitAll
    public RestResponse<List<AccumulatedMeasurementsDTO>> getAccumulatedMeasurementsBetweenDates(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("interval") String interval
    ){
        return RestResponse.ok(
            measurementService.getAccumulatedMeasurementsBetweenDatesAndInterval(
                    startDate, endDate, interval
            )
        );
    }

    @PostMapping("/tags/")
    @PermitAll
    public RestResponse<String> addTagToMeasurements(TagDTO tag){
        try {
            measurementService.addTag(tag.getStartTime(), tag.getEndTime(),
                    tag.getCaption(), tag.getDeviceCategoryName());

        } catch (DeviceCategoryNotFoundException | MeasurementNotFoundException e) {
            //todo: fix no string response
            return RestResponse.notFound();
        } catch (JsonProcessingException e) {
            return RestResponse.serverError();
        }
        return RestResponse.ok("Tag successfully added");
    }


}
