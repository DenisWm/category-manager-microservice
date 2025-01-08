package com.course.admin.catalogo.domain.video;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, Resource aResource);

    ImageMedia storeImage(VideoID anId, Resource aResource);

    void clearResources(VideoID anId);
}
