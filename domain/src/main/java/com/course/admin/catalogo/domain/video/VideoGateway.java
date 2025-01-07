package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    Video update(Video aVideo);

    void deleteById(VideoID anId);

    Optional<Video> findById(VideoID anId);

    Pagination<Video> findAll(VideoSearchQuery aQuery);


}
