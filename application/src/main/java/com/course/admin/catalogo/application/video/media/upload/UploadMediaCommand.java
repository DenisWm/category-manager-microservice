package com.course.admin.catalogo.application.video.media.upload;

import com.course.admin.catalogo.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {

    public static UploadMediaCommand with(final String videoId, final VideoResource videoResource) {
        return new UploadMediaCommand(videoId, videoResource);
    }
}
