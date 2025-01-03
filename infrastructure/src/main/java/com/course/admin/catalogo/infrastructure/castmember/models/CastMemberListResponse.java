package com.course.admin.catalogo.infrastructure.castmember.models;

import com.course.admin.catalogo.domain.castmember.CastMemberType;

public record CastMemberListResponse(
        String id,
        String name,
        String type,
        String createdAt
) {
}
