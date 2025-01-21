package com.course.admin.catalogo.application.video.media.update;

import com.course.admin.catalogo.domain.video.*;

import java.util.Objects;

import static com.course.admin.catalogo.application.utils.IDNotFoundUtils.notFound;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase{

    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aResourceId = aCommand.resourceId();
        final var folder = aCommand.folder();
        final var filename = aCommand.filename();

        final var aVideo = this.videoGateway.findById(anId).orElseThrow(notFound(anId, Video.class));

        final var encodedPath =
                "%s/%s".formatted(folder, filename);

        if (matches(aResourceId, aVideo.getVideo().orElse(null))) {
            updateVideo(VideoMediaType.VIDEO, aCommand.status(), aVideo, encodedPath);
        } else if (matches(aResourceId, aVideo.getTrailer().orElse(null))) {
            updateVideo(VideoMediaType.TRAILER, aCommand.status(), aVideo, encodedPath);
        }
    }

    private void updateVideo(
            final VideoMediaType aType,
            final MediaStatus aStatus,
            final Video aVideo,
            final String encodedPath
    ) {
        switch (aStatus) {
            case PENDING -> {}
            case PROCESSING -> aVideo.processing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);
        }
        this.videoGateway.update(aVideo);
    }

    private boolean matches(final String anId, AudioVideoMedia aMedia) {
        if (aMedia == null) return false;
        return aMedia.id().equals(anId);
    }
}
