package com.course.admin.catalogo.application.video.delete;

import com.course.admin.catalogo.domain.video.MediaResourceGateway;
import com.course.admin.catalogo.domain.video.VideoGateway;
import com.course.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway,
                                     final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String anId) {
        final var videoID = VideoID.from(anId);
        mediaResourceGateway.clearResources(videoID);
        videoGateway.deleteById(videoID);
    }
}
