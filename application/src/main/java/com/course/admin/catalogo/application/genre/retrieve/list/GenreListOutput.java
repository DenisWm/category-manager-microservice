package com.course.admin.catalogo.application.genre.retrieve.list;

import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreID;

import java.time.Instant;

public record GenreListOutput(
        GenreID id,
        String name,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {

    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(aGenre.getId(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
