package com.course.admin.catalogo.domain.events;

@FunctionalInterface
public interface DomainEventPublisher {

     void publish(DomainEvent event);
}
