package at.fhv.master.laendleenergy.datacollector.application.streams.consumer;


import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static at.fhv.master.laendleenergy.datacollector.application.MeasurementParser.parseData;

@ApplicationScoped
public class MeasurementConsumer {


    @Inject
    MeasurementRepository measurementRepository;

    @Incoming("simulator")
    public CompletionStage<Void> processMeasurement(Message<String> message) {
        Optional<Measurement> measurement = parseData(message.getPayload());

        if(measurement.isPresent()){
            CompletableFuture.runAsync(() -> {
                System.out.println(measurement.get());
                measurementRepository.saveMeasurement(measurement.get());
            });
        }
        // Acknowledge the incoming message
        return message.ack();
    }

}
