package com.course.admin.catalogo.application.video.media.get;

import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.video.MediaResourceGateway;
import com.course.admin.catalogo.domain.video.VideoID;
import com.course.admin.catalogo.domain.video.VideoMediaType;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(final GetMediaCommand aCommand) {
        final var videoID = VideoID.from(aCommand.videoId());
        final var aType = VideoMediaType.of(aCommand.mediaType())
                .orElseThrow(typeNotFound(aCommand.mediaType()));
        return mediaResourceGateway
                .getResource(
                        videoID,
                        aType
                )
                .map(MediaOutput::with)
                .orElseThrow(notFound(videoID.getValue(), aType.name()));
    }

    private Supplier<NotFoundException> typeNotFound(final String aType) {
        return () -> NotFoundException.with(new Error("Media type %s does not exists".formatted(aType)));

    }

    private Supplier<NotFoundException> notFound(final String anId, final String aType) {
        return () -> NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
    }
}
