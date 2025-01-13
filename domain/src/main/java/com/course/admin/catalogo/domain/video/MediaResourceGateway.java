package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.resource.Resource;

import java.util.Optional;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);

    ImageMedia storeImage(VideoID anId, VideoResource aResource);

    Optional<Resource> getResource (VideoID anId, VideoMediaType type);

    void clearResources(VideoID anId);
}
