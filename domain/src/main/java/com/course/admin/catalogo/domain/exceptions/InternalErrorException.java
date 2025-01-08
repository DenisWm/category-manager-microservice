package com.course.admin.catalogo.domain.exceptions;

import com.course.admin.catalogo.domain.validation.Error;

import java.util.List;

public class InternalErrorException extends NoStacktraceRuntimeException {

    protected InternalErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public static InternalErrorException with(final String message, final Throwable cause) {
        return new InternalErrorException(message, cause);
    }
}
