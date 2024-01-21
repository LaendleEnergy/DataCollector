package at.fhv.master.laendleenergy.datacollector.application;

import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class MeasurementParser {

    public static Optional<Measurement> parseData(String message) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
        Measurement measurement;
        try {
            measurement = objectMapper.readValue(message, Measurement.class);

            return Optional.of(measurement);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
