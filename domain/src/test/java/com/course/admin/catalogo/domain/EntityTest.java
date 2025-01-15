package com.course.admin.catalogo.domain;

import com.course.admin.catalogo.domain.events.DomainEvent;
import com.course.admin.catalogo.domain.events.DomainEventPublisher;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.utils.InstantUtils;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    public void givenNullAsEvents_whenInstantiate_shouldBeOk() {
        final List<DomainEvent> events = null;
        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
        final List<DomainEvent> events = new ArrayList<>();
        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
        assertNotSame(anEntity.getDomainEvents(), events);
        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = anEntity.getDomainEvents();
            actualEvents.add(InstantUtils::now);
        });
    }

    @Test
    public void givenDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {
        final List<DomainEvent> events = new ArrayList<>();
        final var anEntity = new DummyEntity(new DummyID(), events);
        int expectedEvents = 1;

        anEntity.registerEvent(InstantUtils::now);

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        assertNotSame(anEntity.getDomainEvents(), events);
        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = anEntity.getDomainEvents();
            actualEvents.add(InstantUtils::now);
        });
    }

    @Test
    public void givenDomainEvents_whenCallsPublishDomainEvents_shouldCallPublisherClearEventsList() {
        final List<DomainEvent> events = new ArrayList<>();
        final var expectedEvents = 0;
        final var expectedSentEvents = 2;
        final var counter = new AtomicInteger(0);
        final var anEntity = new DummyEntity(new DummyID(), events);
        anEntity.registerEvent(InstantUtils::now);
        anEntity.registerEvent(InstantUtils::now);

        assertEquals(2, anEntity.getDomainEvents().size());

        anEntity.publishDomainEvents(event -> {
            counter.incrementAndGet();
        });

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        assertEquals(expectedSentEvents, counter.get());
        assertNotSame(anEntity.getDomainEvents(), events);
        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = anEntity.getDomainEvents();
            actualEvents.add(InstantUtils::now);
        });
    }

    public static class DummyID extends Identifier {

        private final String id;

        public DummyID() {
            this.id = IDUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }
    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity(final DummyID dummyID, final List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }
}
