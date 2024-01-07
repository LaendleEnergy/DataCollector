package at.fhv.master.laendleenergy.datacollector.application.services;

import at.fhv.master.laendleenergy.datacollector.controller.dto.AccumulatedMeasurementsDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.AverageMeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.controller.dto.MeasurementDTO;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;

public class DTOMapper {
    public static MeasurementDTO mapMeasurementToMeasurementDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setDeviceId(measurement.getDeviceId() + "");
        measurementDTO.setMeasurementTime(measurement.getTimestamp());
        measurementDTO.setInstantaneousActivePowerPlusW(measurement.getInstantaneousActivePowerPlusW());
        return measurementDTO;
    }

    public static AverageMeasurementDTO mapAverageMeasurmentToAverageMeasurementDTO(AveragedMeasurement averageMeasurement){
        AverageMeasurementDTO averageMeasurementDTO = new AverageMeasurementDTO(
                averageMeasurement.getTimeStart(),
                averageMeasurement.getTimeEnd(),
                averageMeasurement.getDeviceId(),
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
        DeviceCategoryDTO deviceCategoryDTO = new DeviceCategoryDTO(deviceCategory.getCategoryName(),
                deviceCategory.getDescription());
        return deviceCategoryDTO;
    }
}
