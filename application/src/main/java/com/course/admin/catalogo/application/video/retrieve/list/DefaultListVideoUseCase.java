package com.course.admin.catalogo.application.video.retrieve.list;

import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.video.VideoGateway;
import com.course.admin.catalogo.domain.video.VideoPreview;
import com.course.admin.catalogo.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideoUseCase extends ListVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultListVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery aQuery) {
        return videoGateway.findAll(aQuery).map(VideoListOutput::from);
    }
}
