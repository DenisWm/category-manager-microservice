package com.course.admin.catalogo.infrastructure.video.presenters;

import com.course.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.course.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.course.admin.catalogo.application.video.retrieve.list.VideoListOutput;
import com.course.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.course.admin.catalogo.domain.video.AudioVideoMedia;
import com.course.admin.catalogo.domain.video.ImageMedia;
import com.course.admin.catalogo.infrastructure.video.models.*;

public interface VideoApiPresenter {

    static UploadMediaResponse present(final UploadMediaOutput output) {
        return new UploadMediaResponse(
                output.videoId(),
                output.mediaType().name()
        );
    }


    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoResponse(
                output.id()
        );
    }

    static VideoListResponse present(final VideoListOutput output) {
        return new VideoListResponse(
                output.id(),
                output.title(),
                output.description(),
                output.createdAt().toString(),
                output.updatedAt().toString()
        );
    }

    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static ImageMediaResponse present(final ImageMedia image) {
        if (image == null) {
            return null;
        }
        return new ImageMediaResponse(
                image.id(),
                image.checksum(),
                image.name(),
                image.location()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia audioVideo) {
        if (audioVideo == null) {
            return null;
        }
        return new AudioVideoMediaResponse(
                audioVideo.id(),
                audioVideo.checksum(),
                audioVideo.name(),
                audioVideo.rawLocation(),
                audioVideo.encodedLocation(),
                audioVideo.status().name()
        );
    }
}
