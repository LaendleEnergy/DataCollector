package at.fhv.master.laendleenergy.datacollector.application.streams;


import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static at.fhv.master.laendleenergy.datacollector.application.MeasurementParser.parseData;

@ApplicationScoped
public class MeasurementCollector {

    @Incoming("simulator")
    public CompletionStage<Void> processMeasurement(Message<String> message) {
        Optional<Measurement> measurement = parseData(message.getPayload());

        if(measurement.isPresent()){
            //System.out.println(measurement.get().toString());
        }
        // Acknowledge the incoming message
        return message.ack();
    }

}
