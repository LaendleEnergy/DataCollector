package at.fhv.master.laendleenergy.datacollector.application.streams.publisher;


import at.fhv.master.laendleenergy.datacollector.model.events.TaggingCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.redis.client.RedisClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TaggingCreatedEventPublisher {
    @Inject
    RedisClient redisClient;

    @ConfigProperty(name = "redis-tagging-created-key")
    private String key;

    public void publishMessage(TaggingCreatedEvent event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        redisClient.publish(key, objectMapper.writeValueAsString(event));
    }
}