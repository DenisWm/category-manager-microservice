package com.course.admin.catalogo.domain.exceptions;

import com.course.admin.catalogo.domain.AggregateRoot;
import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.validation.Error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException{

    protected NotFoundException(String aMessage, List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(
           final Class<? extends AggregateRoot<?>> anAggregate,
           final Identifier id
    ) {
        final var anErrorMessage = "%s with ID %s was not found".formatted(anAggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anErrorMessage, Collections.emptyList());
    }

    public static NotFoundException with(final Error error) {
        return new NotFoundException(error.message(), Collections.emptyList());
    }


}
