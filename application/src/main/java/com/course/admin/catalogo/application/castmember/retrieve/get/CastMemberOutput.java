package com.course.admin.catalogo.application.castmember.retrieve.get;

import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
    public static CastMemberOutput from(final CastMember aCastMember) {
        return new CastMemberOutput(
                aCastMember.getId().getValue(), aCastMember.getName(),
                CastMemberType.valueOf(aCastMember.getType().name()),
                aCastMember.getCreatedAt(), aCastMember.getUpdatedAt()
        );
    }
}
