package com.course.admin.catalogo.application.castmember.update;

import com.course.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

import static com.course.admin.catalogo.application.utils.IDNotFoundUtils.notFound;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(UpdateCastMemberCommand aCommand) {
        final var id = CastMemberID.from(aCommand.id());
        final var name = aCommand.name();
        final var type = aCommand.type();

        final var aMember = castMemberGateway.findById(id)
                .orElseThrow(notFound(id, CastMember.class));

        final var notification = Notification.create();

        notification.validate(() -> aMember.update(name, type));

        if(notification.hasErrors()){
            notify(id, notification);
        }

        return UpdateCastMemberOutput.from(this.castMemberGateway.update(aMember));
    }

    private static void notify(final Identifier anId, final Notification notification) {
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(anId.getValue()), notification);
    }
}
