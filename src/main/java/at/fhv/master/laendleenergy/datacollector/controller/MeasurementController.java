package at.fhv.master.laendleenergy.datacollector.controller;


import at.fhv.master.laendleenergy.datacollector.application.services.MeasurementService;
import at.fhv.master.laendleenergy.datacollector.controller.dto.TagDTO;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceNotFoundException;
import at.fhv.master.laendleenergy.datacollector.model.exception.MeasurementNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    @Inject
    MeasurementService measurementService;

    
    @GetMapping("/tags/names/all")
    @Operation(summary = "Get tag names / device names for measurements associated with own account (via jwt token) ",
            description = "Returns tag names / device names")
    @APIResponses({
            @APIResponse(
                    responseCode = "200", description = "Extracted tag / device names"),
            @APIResponse(
                    responseCode = "401", description = "Unauthorized"),
            @APIResponse(
                    responseCode = "500", description = "Server Error")}
    )
    @PermitAll
    public RestResponse getTagNames(){
        return RestResponse.ok(measurementService.getAllTagNames());
    }

    @GetMapping("/")
    @Operation(summary = "Get measurements associated with own account (via jwt token) between given dates",
            description = "Returns measurements associated with own account (via jwt token) between given dates.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200", description = "Extracted measurements"),
            @APIResponse(
                    responseCode = "401", description = "Unauthorized"),
            @APIResponse(
                    responseCode = "500", description = "Server Error")}
    )
    @PermitAll
    public RestResponse getMeasurementsBetweenDates(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate){
        return RestResponse.ok(
                measurementService.getMeasurementsBetweenDates(startDate, endDate)
        );
    }

    @GetMapping("/averaged/")
    @Operation(summary = "Get averaged measurements associated with own account (via jwt token) for a given timespan.",
            description = "Returns averaged measurements associated with own account (via jwt token) fora  given timespan.\n" +
                    "Measurements can be averaged within a given number of groups XOR within a given interval (day, week, month, year).")
    @APIResponses({
            @APIResponse(
                    responseCode = "200", description = "Extracted measurements"),
            @APIResponse(
                responseCode = "401", description = "Unauthorized"),
            @APIResponse(
                responseCode = "500", description = "Server Error")}
    )
    @PermitAll
    public RestResponse getMeasurementsBetweenDates(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("numberOfGroups") Integer numberOfGroups, @RequestParam("interval") String interval){
        if(interval != null && numberOfGroups != null){
            return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR,
                    "ERROR: You cannot have set numberOfGroups and interval at the same time!");
        }
        else if(interval != null){
            return RestResponse.ok(
                    measurementService.getAveragedMeasurementsBetweenDatesAndInterval(startDate, endDate, interval)
            );
        }
        return RestResponse.ok(
                measurementService.getAveragedMeasurementsBetweenDates(startDate, endDate, numberOfGroups)
        );
    }


    @GetMapping("/accumulated")
    @Operation(summary = "Get accumulated measurements associated with own account (via jwt token) for given timespan.",
            description = "Returns accumulated measurements associated with own account (via jwt token) for given timespan. \n" +
                    "Measurements can be accumulated within a given interval (day, week, month, year).")
    @APIResponses({
            @APIResponse(
                    responseCode = "200", description = "Extracted measurements"),
            @APIResponse(
                    responseCode = "401", description = "Unauthorized"),
            @APIResponse(
                    responseCode = "500", description = "Server Error")}
    )
    @PermitAll
    public RestResponse getAccumulatedMeasurementsBetweenDates(
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
    @Operation(summary = "Add tag to measurements associated with own account (via jwt token) for given timespan",
            description = "Returns if adding tag to measurements associated with own account (via jwt token) was successful.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200", description = "Success message"),
            @APIResponse(
                    responseCode = "401", description = "Unauthorized"),
            @APIResponse(
                    responseCode = "500", description = "Server Error")}
    )
    @PermitAll
    public RestResponse addTagToMeasurements(TagDTO tag){
        try {
            measurementService.addTag(tag.getStartTime(), tag.getEndTime(),
                    tag.getDeviceName(), tag.getDeviceCategoryName());

        } catch (DeviceCategoryNotFoundException | MeasurementNotFoundException | DeviceNotFoundException e) {
            return RestResponse.status(RestResponse.Status.NOT_FOUND, e.toString());
        } catch (JsonProcessingException e) {
            return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR,
                    "Error: Parsing response failed.");
        }
        return RestResponse.ok("Tag successfully added");
    }


}
