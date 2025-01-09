package com.course.admin.catalogo.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CastMemberResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at")  String updatedAt
) {


}
