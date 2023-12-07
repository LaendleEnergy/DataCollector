package at.fhv.master.laendleenergy.datacollector.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import at.fhv.master.laendleenergy.datacollector.model.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.TimeZone;

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
