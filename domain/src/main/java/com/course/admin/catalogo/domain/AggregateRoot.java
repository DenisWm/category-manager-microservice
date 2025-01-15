package com.course.admin.catalogo.domain;

import com.course.admin.catalogo.domain.events.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    public AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    public AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}
