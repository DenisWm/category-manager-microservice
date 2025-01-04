package com.course.admin.catalogo.infrastructure.castmember.models;

import com.course.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
