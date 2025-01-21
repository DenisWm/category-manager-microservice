package com.course.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UploadMediaResponse(
        @JsonProperty("video_id") String videoId,
        @JsonProperty("media_type") String mediaType
) {
}
