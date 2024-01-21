package at.fhv.master.laendleenergy.datacollector.application.streams.consumer;

import at.fhv.master.laendleenergy.datacollector.application.streams.EventHandler;
import at.fhv.master.laendleenergy.datacollector.model.events.account.HouseholdUpdatedEvent;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceAddedEvent;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import jakarta.inject.Inject;

import java.util.function.Consumer;

import at.fhv.master.laendleenergy.datacollector.application.streams.EventHandler;
import at.fhv.master.laendleenergy.datacollector.model.events.household.DeviceCategoryAddedEvent;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.function.Consumer;

public class HouseholdUpdatedEventConsumer implements Consumer<HouseholdUpdatedEvent> {
    @Inject
    EventHandler eventHandler;

    private final PubSubCommands<HouseholdUpdatedEvent> pub;
    private final PubSubCommands.RedisSubscriber subscriber;


    public HouseholdUpdatedEventConsumer(RedisDataSource ds) {
        pub = ds.pubsub(HouseholdUpdatedEvent.class);
        subscriber = pub.subscribe("HouseholdUpdatedEvent", this);
    }

    @Override
    public void accept(HouseholdUpdatedEvent householdUpdatedEvent) {
        eventHandler.handleHouseholdUpdatedEvent(householdUpdatedEvent);
    }
}
