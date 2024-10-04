package com.course.admin.catalogo.application.castmember.update;

import com.course.admin.catalogo.domain.castmember.CastMember;

public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final CastMember castMember) {
        return new UpdateCastMemberOutput(castMember.getId().getValue());
    }
}
