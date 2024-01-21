package at.fhv.master.laendleenergy.datacollector.model.exception;

public class MeasurementNotFoundException extends Exception {
    @Override
    public String toString() {
        return "Error: There are no measurements for the given time!";
    }
}
