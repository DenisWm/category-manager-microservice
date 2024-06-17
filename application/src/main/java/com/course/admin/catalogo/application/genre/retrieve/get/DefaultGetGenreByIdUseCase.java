package com.course.admin.catalogo.application.genre.retrieve.get;

import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

import static com.course.admin.catalogo.application.utils.IDNotFoundUtils.notFound;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase{

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(String anId) {
        final var id = GenreID.from(anId);
        return genreGateway.findById(id)
                .map(GenreOutput::from)
                .orElseThrow(notFound(id, Genre.class));
    }
}
