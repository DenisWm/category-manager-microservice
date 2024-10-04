package com.course.admin.catalogo.application.castmember.create;

import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var name = aCommand.name();
        final var type = aCommand.type();

        final var notification = Notification.create();

        final var aMember = notification.validate(() -> CastMember.newMember(name, type));

        if(notification.hasErrors()){
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private static void notify(Notification notification) {
        throw new NotificationException("Could not create Aggregate CastMember", notification);
    }
}
