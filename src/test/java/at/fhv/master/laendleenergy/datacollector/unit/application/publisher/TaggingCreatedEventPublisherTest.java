package at.fhv.master.laendleenergy.datacollector.unit.application.publisher;


import at.fhv.master.laendleenergy.datacollector.application.streams.publisher.TaggingCreatedEventPublisher;
import at.fhv.master.laendleenergy.datacollector.model.events.TaggingCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;


@QuarkusTest
@TestTransaction
public class TaggingCreatedEventPublisherTest {
    @Inject
    TaggingCreatedEventPublisher pub;

    @Test
    public void testConnection() throws JsonProcessingException {
        TaggingCreatedEvent event = new TaggingCreatedEvent("u1", "d1", "h1");
        pub.publishMessage(event);
    }
}
