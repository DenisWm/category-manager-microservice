package com.course.admin.catalogo.application.video.retrieve.get;

import com.course.admin.catalogo.domain.video.Video;
import com.course.admin.catalogo.domain.video.VideoGateway;
import com.course.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

import static com.course.admin.catalogo.application.utils.IDNotFoundUtils.notFound;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anId) {
        final var id = VideoID.from(anId);
        return videoGateway.findById(id).map(VideoOutput::from).orElseThrow(notFound(id, Video.class));
    }
}
