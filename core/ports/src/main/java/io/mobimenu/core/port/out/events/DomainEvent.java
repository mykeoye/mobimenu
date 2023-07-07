package io.mobimenu.core.port.out.events;

/**
 * Represents a domain event on a system. A domain event is a notification of something
 * important that has happened on the system
 *
 * @param eventType     the type of event we want to notify observers of
 * @param <T>           the type T of the payload
 * @param payload       payload that should be sent as part of the event
 */
public record DomainEvent<T>(EventType eventType, T payload) {

    public static <T> DomainEvent<T> of(EventType eventType, T payload) {
        return new DomainEvent<>(eventType, payload);
    }
}
