package com.course.admin.catalogo.application.genre.delete;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteGenreUseCaseIT {
    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;
    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        final var aGenre = genreGateway.create(Genre.newGenre("Ação", true));

        assertEquals(1, genreRepository.count());

        final var expectedId = aGenre.getId();

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var expectedId = GenreID.from("invalid");
        genreGateway.create(Genre.newGenre("Ação", true));

        assertEquals(1, genreRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(1, genreRepository.count());

    }
}
