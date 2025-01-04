package com.course.admin.catalogo.application.castmember.retrieve.list;

import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {
    public static CastMemberListOutput from(final CastMember aCastMember) {
        return new CastMemberListOutput(
                aCastMember.getId().getValue(), aCastMember.getName(),
                CastMemberType.valueOf(aCastMember.getType().name()),
                aCastMember.getCreatedAt()
        );
    }
}
