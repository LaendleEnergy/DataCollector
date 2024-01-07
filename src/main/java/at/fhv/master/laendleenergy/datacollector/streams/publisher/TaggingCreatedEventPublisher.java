package at.fhv.master.laendleenergy.datacollector.streams.publisher;


import at.fhv.master.laendleenergy.datacollector.model.events.TaggingCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.redis.client.RedisClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TaggingCreatedEventPublisher {
    @Inject
    RedisClient redisClient;

    public void publishMessage(TaggingCreatedEvent event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        redisClient.publish("HouseholdUpdatedEvent", objectMapper.writeValueAsString(event));
    }
}