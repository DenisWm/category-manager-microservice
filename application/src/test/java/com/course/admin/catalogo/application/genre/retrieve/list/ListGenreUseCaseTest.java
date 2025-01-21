package com.course.admin.catalogo.application.genre.retrieve.list;

import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenreUseCase useCase;
    @Mock
    private GenreGateway genreGateway;
    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAnValidQuery_whenCallsListGenre_shouldReturnGenres() {
        final var genres = List.of(
                Genre.newGenre("Ação", true),
                Genre.newGenre("Aventura", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = genres.stream().map(GenreListOutput::from).toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        when(genreGateway.findAll(any())).thenReturn(expectedPagination);
        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAnValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var genres = List.<Genre>of(
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems =  List.<GenreListOutput>of(
        );

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        when(genreGateway.findAll(any())).thenReturn(expectedPagination);
        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.currentPage());
        assertEquals(expectedItems, actualOutput.items());

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAnValidQuery_whenCallsListGenreAndGatewayThrowsRandom_shouldThrowsException() {

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(genreGateway).findAll(any());

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }
}
