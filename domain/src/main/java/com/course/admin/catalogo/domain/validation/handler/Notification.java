package com.course.admin.catalogo.domain.validation.handler;

import com.course.admin.catalogo.domain.exceptions.DomainException;
import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;
    @Override
    public Notification append(Error anError) {
        this.errors.add(anError);
        return this;
    }

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }
    public static Notification create() {
        return new Notification(new ArrayList<>());
    }
    public static Notification create(Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }
    @Override
    public ValidationHandler append(final ValidationHandler aHandler) {
        this.errors.addAll(aHandler.getErrors());
        return this;
    }

    @Override
    public ValidationHandler validate(Validation aValidation) {
        try {
            aValidation.validate();

        } catch (DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
