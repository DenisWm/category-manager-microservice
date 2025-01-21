package com.course.admin.catalogo.domain.genre;

import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre genre);
    void deleteById(GenreID anId);
    Optional<Genre> findById(GenreID anId);
    Genre update(Genre genre);
    Pagination<Genre> findAll(SearchQuery aQuery);
    List<GenreID> existsByIds(Iterable<GenreID> ids);
}
