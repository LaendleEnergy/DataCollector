package at.fhv.master.laendleenergy.datacollector.unit.application.publisher;

import at.fhv.master.laendleenergy.datacollector.application.streams.publisher.DeviceCategoryAddedEventPublisher;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceCategoryAddedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestTransaction
public class DeviceCategoryAddedEventPublisherTest {
    @Inject
    DeviceCategoryAddedEventPublisher pub;

    @Test
    public void testConnection() throws JsonProcessingException {
        DeviceCategoryAddedEvent event = new DeviceCategoryAddedEvent("Waschmaschine", "d1", "h1", "m1");
        pub.publishMessage(event);
    }
}
