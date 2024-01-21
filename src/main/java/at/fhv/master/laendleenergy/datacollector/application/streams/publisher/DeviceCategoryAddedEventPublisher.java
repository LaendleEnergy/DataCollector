package at.fhv.master.laendleenergy.datacollector.application.streams.publisher;

import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceCategoryAddedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Startup
public class DeviceCategoryAddedEventPublisher {
    @Inject
    RedisClient redisClient;
    @ConfigProperty(name = "redis-device-category-added-key")
    private String KEY;

    public DeviceCategoryAddedEventPublisher(){}

    public void publishMessage(DeviceCategoryAddedEvent message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        redisClient.publish(KEY, objectMapper.writeValueAsString(message));
    }
}