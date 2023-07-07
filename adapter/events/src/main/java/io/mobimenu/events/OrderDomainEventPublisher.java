package io.mobimenu.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mobimenu.core.port.out.events.DomainEvent;
import io.mobimenu.domain.Order;
import io.smallrye.mutiny.unchecked.Unchecked;
import lombok.RequiredArgsConstructor;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.mobimenu.core.port.out.events.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OrderDomainEventPublisher implements DomainEventPublisher<Order> {

    private static final String BUS_ADDRESS = "domain-events";
    private final ObjectMapper mapper;
    private final EventBus eventBus;

    @Override
    public void publish(DomainEvent<Order> event) {
        log.info("Received domain event: {}", event);
        eventBus.request(BUS_ADDRESS, Unchecked.supplier(() -> mapper.writeValueAsString(event)))
                .subscribe()
                .with(
                    message -> log.info("Event sent to bus address {}", message.address()),
                    throwable -> log.error("Exception sending domain event {}, Exception ", event, throwable)
                );
    }
}
