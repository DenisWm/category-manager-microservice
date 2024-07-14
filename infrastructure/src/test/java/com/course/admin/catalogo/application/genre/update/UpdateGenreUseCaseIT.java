package com.course.admin.catalogo.application.genre.update;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.course.admin.catalogo.application.utils.IDUtils.asString;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private CategoryGateway categoryGateway;
    @Autowired
    private UpdateGenreUseCase useCase;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));
        final var expectedId = aGenre.getId();

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));


        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true).addCategories(List.of(filmes.getId(), series.getId())));

        final var expectedId = aGenre.getId();

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                filmes.getId(),
                series.getId()
        );

        final var aCommand =
                UpdateGenreCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();

        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        assertNull(aGenre.getDeletedAt());
        assertTrue(aGenre.isActive());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
    }
    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
        );
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateGenreCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

    }
    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var series = categoryGateway.create(Category.newCategory("Series", null, true));


        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));
        final var expectedId = aGenre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                series.getId(), filmes.getId(), CategoryID.from("456")
        );
        final var expectedErrorMessageOne = "Some categories could not be found: 456";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand =
                UpdateGenreCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

    }
}
