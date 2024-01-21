package at.fhv.master.laendleenergy.datacollector.model.exception;

public class DeviceCategoryNotFoundException extends Exception {
    @Override
    public String toString() {
        return "Error: DeviceCategory for given id not found!";
    }
}
