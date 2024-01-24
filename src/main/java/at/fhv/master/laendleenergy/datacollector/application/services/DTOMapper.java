package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.dto.*;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;

import java.util.stream.Collectors;

public class DTOMapper {
    public static MeasurementDTO mapMeasurementToMeasurementDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setDeviceId(measurement.getDeviceId() + "");
        measurementDTO.setMeasurementTime(measurement.getTimestamp());
        measurementDTO.setInstantaneousActivePowerPlusW(measurement.getInstantaneousActivePowerPlusW());
        measurementDTO.setTagDTOList(
                measurement.getTags().stream().map(
                        tag -> {
                            return new TagDTO(tag.getStarTime(),
                                    tag.getEndTime(),
                                    tag.getDeviceName().replace("_", " "),
                                    tag.getDeviceCategoryName().replace("_", " ")
                            );
                        }
                ).collect(Collectors.toList())
        );
        //todo: map other measurement fields (have no use in the frontend as of now)
        return measurementDTO;
    }

    public static AverageMeasurementDTO mapAverageMeasurmentToAverageMeasurementDTO(AveragedMeasurement averageMeasurement){
        AverageMeasurementDTO averageMeasurementDTO = new AverageMeasurementDTO(
                averageMeasurement.getTimeStart(),
                averageMeasurement.getTimeEnd(),
                averageMeasurement.getMeterDeviceId(),
                averageMeasurement.getCurrentL1AAvg(),
                averageMeasurement.getCurrentL2AAvg(),
                averageMeasurement.getCurrentL3AAvg(),
                averageMeasurement.getVoltageL1VAvg(),
                averageMeasurement.getVoltageL2VAvg(),
                averageMeasurement.getVoltageL3VAvg(),
                averageMeasurement.getInstantaneousActivePowerPlusWAvg(),
                averageMeasurement.getInstantaneousActivePowerMinusWAvg()
        );

        return averageMeasurementDTO;
    }

    public static AccumulatedMeasurementsDTO mapAccumulatedMeasurementToAccumulatedMeasurementDTO(AccumulatedMeasurements accumulatedMeasurement){
        AccumulatedMeasurementsDTO accumulatedMeasurementDTO = new AccumulatedMeasurementsDTO(
                accumulatedMeasurement.getTimeStart(),
                accumulatedMeasurement.getTimeEnd(),
                accumulatedMeasurement.getDeviceId(),
                accumulatedMeasurement.getEnergyConsumedWh(),
                accumulatedMeasurement.getEnergyDeliveredWh(),
                accumulatedMeasurement.getEnergyConsumedPriceEuro()
        );
        return accumulatedMeasurementDTO;
    }


    public static DeviceCategoryDTO mapDeviceCategoryToDeviceCategoryDTO(DeviceCategory deviceCategory) {
        DeviceCategoryDTO deviceCategoryDTO = new DeviceCategoryDTO(deviceCategory.getCategoryName());
        return deviceCategoryDTO;
    }
}
