package com.course.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record UpdateVideoResponse(
        @JsonProperty("id") String id
) {

    public static UpdateVideoResponse with(final String id) {
        return new UpdateVideoResponse(id);
    }
}
