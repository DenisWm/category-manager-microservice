package com.course.admin.catalogo.application.utils;

import com.course.admin.catalogo.domain.AggregateRoot;
import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.exceptions.DomainException;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreID;

import java.util.function.Supplier;

public final class IDNotFoundUtils {
    public static Supplier<DomainException> notFound(final Identifier anId, Class<? extends AggregateRoot<?>> anAggregate) {
        return () -> NotFoundException.with(anAggregate, anId);
    }
}
