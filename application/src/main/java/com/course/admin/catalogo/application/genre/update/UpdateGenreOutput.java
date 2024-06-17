package com.course.admin.catalogo.application.genre.update;

import com.course.admin.catalogo.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {

    public static UpdateGenreOutput from(final String anId) {
        return new UpdateGenreOutput(anId);
    }
    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
