package at.fhv.master.laendleenergy.datacollector.unit.application.consumer;

import org.eclipse.microprofile.reactive.messaging.Message;
import at.fhv.master.laendleenergy.datacollector.application.streams.consumer.MeasurementConsumer;
import at.fhv.master.laendleenergy.datacollector.model.repositories.MeasurementRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;

@QuarkusTest
@TestTransaction
public class MeasurementConsumerTests {
    @Inject
    MeasurementConsumer consumer;
    @InjectMock
    MeasurementRepository measurementRepository;

    @Test
    public void testProcessMeasurement() {
       String messageString = "{\n" +
               "  \"measurementId\": {\n" +
               "    \"deviceId\": \"exampleMeterDeviceId\",\n" +
               "    \"timestamp\": \"2024-01-24T12:34:56\"\n" +
               "  },\n" +
               "  \"currentL1A\": 10.5,\n" +
               "  \"currentL2A\": 15.2,\n" +
               "  \"currentL3A\": 20.7,\n" +
               "  \"voltageL1V\": 220.0,\n" +
               "  \"voltageL2V\": 230.0,\n" +
               "  \"voltageL3V\": 240.0,\n" +
               "  \"instantaneousActivePowerPlusW\": 500.0,\n" +
               "  \"instantaneousActivePowerMinusW\": 50.0,\n" +
               "  \"totalEnergyConsumedWh\": 1500.0,\n" +
               "  \"totalEnergyDeliveredWh\": 1200.0" +
               "}\n";

        Message<String> message = new Message<String>() {
            @Override
            public String getPayload() {
                return messageString;
            }
        };
        consumer.processMeasurement(message);
        Mockito.verify(measurementRepository, timeout(100).times(1)).saveMeasurement(any());
    }
}