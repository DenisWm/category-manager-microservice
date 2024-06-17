package com.course.admin.catalogo.application.genre.update;

import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.course.admin.catalogo.application.utils.IDUtils.asString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateGenreUseCaseTest extends UseCaseTest {
    @Mock
    private GenreGateway genreGateway;
    @Mock
    private CategoryGateway categoryGateway;
    @InjectMocks
    public DefaultUpdateGenreUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }
    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                && Objects.equals(expectedName, aUpdatedGenre.getName())
                && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                && Objects.isNull(aUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                CategoryID.from("123"),
                CategoryID.from("456 ")
        );

        final var aCommand =
                UpdateGenreCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);
        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(aUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();

        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        assertNull(aGenre.getDeletedAt());
        assertTrue(aGenre.isActive());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.nonNull(aUpdatedGenre.getDeletedAt())
        ));
    }
    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        final var aGenre = Genre.newGenre("acao", true);
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

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).update(any());
    }
    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var series = CategoryID.from("123");
        final var filmes = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                series, filmes, documentarios
        );
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand =
                UpdateGenreCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(categoryGateway.existsByIds(any())).thenReturn(List.of(series));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(0)).update(any());
    }
}
