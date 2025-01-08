package com.course.admin.catalogo.application.video.retrieve.list;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideoUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
