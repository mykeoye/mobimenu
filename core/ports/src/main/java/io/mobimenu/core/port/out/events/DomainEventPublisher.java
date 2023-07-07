package io.mobimenu.core.port.out.events;

/**
 * This interface abstracts the creation of a domain event. A domain event is a notification of
 * something that has happened on the system, which a set of observers will like to know about
 *
 * @param <T>   the payload type of the event
 */
public interface DomainEventPublisher<T> {

    /**
     * Publishes a domain event
     *
     * @param event the event to publish
     */
    void publish(DomainEvent<T> event);
}
