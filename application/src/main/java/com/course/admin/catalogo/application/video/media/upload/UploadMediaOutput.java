package com.course.admin.catalogo.application.video.media.upload;

import com.course.admin.catalogo.domain.video.Video;
import com.course.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {
    public static UploadMediaOutput with(final Video aVideo, final VideoMediaType mediaType) {
        return new UploadMediaOutput(aVideo.getId().getValue(), mediaType);
    }
}
