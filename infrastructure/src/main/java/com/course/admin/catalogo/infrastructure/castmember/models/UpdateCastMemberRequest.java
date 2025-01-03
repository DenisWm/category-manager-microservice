package com.course.admin.catalogo.infrastructure.castmember.models;

import com.course.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}
