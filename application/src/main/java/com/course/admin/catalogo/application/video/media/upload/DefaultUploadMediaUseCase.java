package com.course.admin.catalogo.application.video.media.upload;

import com.course.admin.catalogo.domain.video.MediaResourceGateway;
import com.course.admin.catalogo.domain.video.Video;
import com.course.admin.catalogo.domain.video.VideoGateway;
import com.course.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

import static com.course.admin.catalogo.application.utils.IDNotFoundUtils.notFound;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUploadMediaUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aResource = aCommand.videoResource();

        final var aVideo = videoGateway.findById(anId)
                .orElseThrow(notFound(anId, Video.class));

        switch (aResource.type()) {
            case VIDEO -> aVideo.updateVideoMedia(this.mediaResourceGateway.storeAudioVideo(anId, aResource));
            case TRAILER -> aVideo.updateTrailerMedia(this.mediaResourceGateway.storeAudioVideo(anId, aResource));
            case BANNER -> aVideo.updateBannerMedia(this.mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL -> aVideo.updateThumbnailMedia(this.mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL_HALF -> aVideo.updateThumbnailHalfMedia(this.mediaResourceGateway.storeImage(anId, aResource));
        }

        return UploadMediaOutput.with(videoGateway.update(aVideo), aResource.type());
    }
}
