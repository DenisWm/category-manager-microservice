package com.course.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);
    ValidationHandler append(ValidationHandler aHandler);
    ValidationHandler validate(Validation aValidation);
    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }
    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);

        } else {
            return null;
        }
    }

    List<Error> getErrors();
    interface Validation{
        void validate();
    }
}
