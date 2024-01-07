package at.fhv.master.laendleenergy.datacollector.application.enums;

public enum Interval {
    HOUR,
    DAY,
    WEEK,
    YEAR;
    @Override
    public String toString() {
        switch (this){
            case DAY -> {
                return "day";
            }
            case WEEK -> {
                return "week";
            }
            case YEAR -> {
                return "year";
            }
            default -> {
                return "hour";
            }
        }
    }
}
