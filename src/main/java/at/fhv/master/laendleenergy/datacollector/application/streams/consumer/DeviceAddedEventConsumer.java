package at.fhv.master.laendleenergy.datacollector.application.streams.consumer;

import at.fhv.master.laendleenergy.datacollector.application.streams.EventHandler;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceAddedEvent;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceCategoryAddedEvent;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.function.Consumer;


@ApplicationScoped
@Startup
public class DeviceAddedConsumer implements Consumer<DeviceAddedEvent> {
    @Inject
    EventHandler eventHandler;

    private final PubSubCommands<DeviceAddedEvent> pub;
    private final PubSubCommands.RedisSubscriber subscriber;


    public DeviceAddedConsumer(RedisDataSource ds) {
        pub = ds.pubsub(DeviceAddedEvent.class);
        subscriber = pub.subscribe("DeviceAddedEvent", this);
    }

    @Override
    public void accept(DeviceAddedEvent deviceAddedEvent) {
        eventHandler.handleDeviceAddedEvent(deviceAddedEvent);
    }

}
