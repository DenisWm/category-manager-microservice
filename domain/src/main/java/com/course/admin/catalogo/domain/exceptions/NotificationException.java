package com.course.admin.catalogo.domain.exceptions;

import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.validation.handler.Notification;

import java.util.List;

public class NotificationException extends DomainException {

    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
