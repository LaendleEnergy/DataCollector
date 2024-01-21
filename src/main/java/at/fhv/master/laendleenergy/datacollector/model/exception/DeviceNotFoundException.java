package at.fhv.master.laendleenergy.datacollector.model.exception;

public class DeviceNotFoundException extends Exception {
    @Override
    public String toString() {
        return "Error: Device for given id not found!";
    }
}
