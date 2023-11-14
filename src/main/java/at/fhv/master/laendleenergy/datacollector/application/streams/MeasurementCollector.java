package at.fhv.master.laendleenergy.datacollector.application.streams;


import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

import static at.fhv.master.laendleenergy.datacollector.application.MeasurementParser.parseData;

@ApplicationScoped
public class MeasurementCollector {

    @Incoming("simulator")
    public CompletionStage<Void> processMeasurement(Message<byte[]> message) {
        // process your price.
        System.out.println(parseData(message.getPayload()));
        // Acknowledge the incoming message
        return message.ack();
    }

}
