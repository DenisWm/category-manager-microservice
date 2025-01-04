package com.course.admin.catalogo.infrastructure.castmember.models;

import com.course.admin.catalogo.domain.castmember.CastMemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CastMemberListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") String createdAt
) {
}
