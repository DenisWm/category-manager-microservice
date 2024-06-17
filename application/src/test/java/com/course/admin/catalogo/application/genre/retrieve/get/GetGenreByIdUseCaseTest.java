package com.course.admin.catalogo.application.genre.retrieve.get;

import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.course.admin.catalogo.application.utils.IDUtils.asString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetGenreByIdUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;
    @Mock
    private GenreGateway genreGateway;
    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetById_ShouldReturnGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();
        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre));

        final var actualGenre = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(asString(expectedCategories), actualGenre.categories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsGetByIdAndDoesNotExists_ShouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedErrorCount = 1;
        final var expectedId = GenreID.from("123");


        when(genreGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue()
        ));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1)).findById(eq(expectedId));
    }
}
