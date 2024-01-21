package at.fhv.master.laendleenergy.datacollector.application.enums;

public enum Interval {
    MONTH,
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
            case MONTH -> {
                return "month";
            }
            default -> {
                return "hour";
            }
        }
    }
}
