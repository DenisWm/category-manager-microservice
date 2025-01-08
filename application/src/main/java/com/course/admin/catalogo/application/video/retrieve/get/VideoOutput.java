package com.course.admin.catalogo.application.video.retrieve.get;

import com.course.admin.catalogo.domain.video.AudioVideoMedia;
import com.course.admin.catalogo.domain.video.ImageMedia;
import com.course.admin.catalogo.domain.video.Rating;
import com.course.admin.catalogo.domain.video.Video;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.course.admin.catalogo.application.utils.IDUtils.asString;

public record VideoOutput(
        String id,
        String title,
        String description,
        int launchedAt,
        double duration,
        Rating rating,
        boolean opened,
        boolean published,
        Instant createdAt,
        Instant updatedAt,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        AudioVideoMedia video,
        AudioVideoMedia trailer,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf
) {
    public static VideoOutput from(final Video aVideo) {
        return new VideoOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getDuration(),
                aVideo.getRating(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                new HashSet<>(asString(aVideo.getCategories())),
                new HashSet<>(asString(aVideo.getGenres())),
                new HashSet<>(asString(aVideo.getCastMembers())),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbNail().orElse(null),
                aVideo.getThumbNailHalf().orElse(null)
        );
    }
}
