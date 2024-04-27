package com.course.admin.catalogo.domain.exceptions;

import com.course.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException {

    private final List<Error> errors;
    public DomainException(final List<Error> anErrors) {
        super("", null, true, false);
        this.errors = anErrors;
    }
    public static DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors);
    }
    public List<Error> getErrors() {
        return errors;
    }
}
