package com.course.admin.catalogo.domain.video;

import java.time.Instant;

public record VideoPreview(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static VideoPreview from(final Video video) {
        return new VideoPreview(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getCreatedAt(),
                video.getUpdatedAt()
        );
    }
}
