package com.course.admin.catalogo.application.video.retrieve.list;

import com.course.admin.catalogo.domain.video.AudioVideoMedia;
import com.course.admin.catalogo.domain.video.ImageMedia;
import com.course.admin.catalogo.domain.video.Rating;
import com.course.admin.catalogo.domain.video.Video;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.course.admin.catalogo.application.utils.IDUtils.asString;

public record VideoListOutput(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt

) {
    public static VideoListOutput from(final Video aVideo) {
        return new VideoListOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }
}
