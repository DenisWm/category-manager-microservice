package com.course.admin.catalogo.application.genre.retrieve.get;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.course.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Optional;

import static com.course.admin.catalogo.application.utils.IDUtils.asString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetById_ShouldReturnGenre() {
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                series.getId(),
                filmes.getId()
        );

        final var aGenre = genreGateway.create(Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories));


        final var expectedId = aGenre.getId();

        final var actualGenre = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.categories().size() &&
                asString(expectedCategories).containsAll(actualGenre.categories())
        );
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
    }
    @Test
    public void givenAValidId_whenCallsGetByIdAndDoesNotExists_ShouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        final var actualException = assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue()
                ));

        assertEquals(expectedErrorMessage, actualException.getMessage());

    }
}
