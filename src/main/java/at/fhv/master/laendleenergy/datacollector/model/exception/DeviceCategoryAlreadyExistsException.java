package at.fhv.master.laendleenergy.datacollector.model.exception;

public class DeviceCategoryAlreadyExistsException extends Exception {
    @Override
    public String toString() {
        return "Error: DeviceCategory already exists!!";
    }
}
