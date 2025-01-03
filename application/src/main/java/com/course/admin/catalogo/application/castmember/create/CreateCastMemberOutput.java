package com.course.admin.catalogo.application.castmember.create;

import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberID;

public record CreateCastMemberOutput(String id) {

    public static CreateCastMemberOutput from(final CastMember castMember) {
        return from(castMember.getId());
    }

    public static CreateCastMemberOutput from(final CastMemberID anId) {
        return new CreateCastMemberOutput(anId.getValue());
    }
}
