package com.course.admin.catalogo.application.castmember.retrieve.get;

import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;

import java.util.Objects;
import java.util.Optional;

import static com.course.admin.catalogo.application.utils.IDNotFoundUtils.notFound;

public non-sealed class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(final String anId) {
        final var aMemberId = CastMemberID.from(anId);
        return castMemberGateway.findById(aMemberId)
                .map(CastMemberOutput::from)
                        .orElseThrow(
                                notFound(
                                        aMemberId,
                                        CastMember.class
                                )
                        );

    }
}
