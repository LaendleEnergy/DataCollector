package at.fhv.master.laendleenergy.datacollector.unit.application.consumer;

import at.fhv.master.laendleenergy.datacollector.application.streams.EventHandler;
import at.fhv.master.laendleenergy.datacollector.application.streams.consumer.DeviceAddedEventConsumer;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceAddedEvent;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.timeout;

@QuarkusTest
@TestTransaction
public class DeviceAddedEventConsumerTests {
    @Inject
    DeviceAddedEventConsumer consumer;
    @InjectMock
    EventHandler eventHandler;

    @Test
    public void testAccept() {
        DeviceAddedEvent event = new DeviceAddedEvent();
        consumer.accept(event);

        Mockito.verify(eventHandler, timeout(100).times(1)).handleDeviceAddedEvent(event);
    }
}