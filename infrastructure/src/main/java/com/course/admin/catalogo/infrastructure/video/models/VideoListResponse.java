package com.course.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record VideoListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
