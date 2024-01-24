package at.fhv.master.laendleenergy.datacollector.unit.application.consumer;

import at.fhv.master.laendleenergy.datacollector.application.streams.EventHandler;
import at.fhv.master.laendleenergy.datacollector.application.streams.consumer.DeviceAddedEventConsumer;
import at.fhv.master.laendleenergy.datacollector.application.streams.consumer.HouseholdUpdatedEventConsumer;
import at.fhv.master.laendleenergy.datacollector.model.events.account.HouseholdUpdatedEvent;
import at.fhv.master.laendleenergy.datacollector.model.events.account.domain.UpdatedHousehold;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceAddedEvent;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.mockito.Mockito.timeout;

@QuarkusTest
@TestTransaction
public class HouseholdUpdatedEventConsumerTests {
    @Inject
    HouseholdUpdatedEventConsumer consumer;
    @InjectMock
    EventHandler eventHandler;

    @Test
    public void testAccept() {
        HouseholdUpdatedEvent event = new HouseholdUpdatedEvent("id1", new UpdatedHousehold(), LocalDateTime.now());
        consumer.accept(event);
        Mockito.verify(eventHandler, timeout(100).times(1)).handleHouseholdUpdatedEvent(event);
    }
}