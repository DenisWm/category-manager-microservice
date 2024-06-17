package com.course.admin.catalogo.application.genre.retrieve.list;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
