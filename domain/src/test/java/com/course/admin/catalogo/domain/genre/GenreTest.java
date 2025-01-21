package com.course.admin.catalogo.domain.genre;

import com.course.admin.catalogo.domain.UnitTest;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.exceptions.DomainException;
import com.course.admin.catalogo.domain.validation.handler.Notification;
import com.course.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallNewGenre_thenInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;


        final var actualException = assertThrows(
                NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenInvalidEmpty_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        final String expectedName = "";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }


    @Test
    public void givenAnInvalidNameLengthMoraThan255_whenCallNewGenreAndValidate_thenShouldReceiveError() {
        final String expectedName = """
                Caros amigos, o comprometimento entre as equipes nos obriga à análise das condições financeiras e administrativas exigidas. 
                Por outro lado, a complexidade dos estudos efetuados prepara-nos para enfrentar situações atípicas decorrentes do orçamento setorial. 
                Gostaria de enfatizar que o entendimento das metas propostas obstaculiza a apreciação da importância das formas de ação. 
                Por conseguinte, a estrutura atual da organização talvez venha a ressaltar a relatividade das posturas dos órgãos dirigentes com relação às suas atribuições.
                """;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualException =
                assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }
    @Test
    public void givenAValidFalseIsActive_whenCallNewGenreAndValidate_thenInstantiateAGenre() {
        final String expectedName = "Ação";
        final var expectedIsActive = false;
        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        assertDoesNotThrow(() -> actualGenre.validate(new ThrowsValidationHandler()));

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
    }
    @Test
    public void givenAValidActiveGenre_whenCallDeactivate_thenReturnGenreInactivated() {
        final String expectedName = "Ação";
        final var expectedIsActive = false;

        final var aGenre = Genre.newGenre(expectedName, true);

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        final var createdAt = aGenre.getCreatedAt();
        final var updatedAt = aGenre.getUpdatedAt();

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var actualGenre = aGenre.deactivate();

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(createdAt, actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));

    }
    @Test
    public void givenAValidInactiveGenre_whenCallActivate_thenReturnGenreActivated() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;

        final var aGenre = Genre.newGenre(expectedName, false);

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        final var createdAt = aGenre.getCreatedAt();
        final var updatedAt = aGenre.getUpdatedAt();

        assertFalse(aGenre.isActive());
        assertNotNull(aGenre.getDeletedAt());

        final var actualGenre = aGenre.activate();

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(createdAt, actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualGenre.getDeletedAt());
    }
    @Test
    public void givenAValidInactiveGenre_whenCallUpdateWithActivate_thenReturnGenreUpdated() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var aGenre = Genre.newGenre("acao", false);

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        final var createdAt = aGenre.getCreatedAt();
        final var updatedAt = aGenre.getUpdatedAt();

        final var actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(createdAt, actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualGenre.getDeletedAt());

    }
    @Test
    public void givenAValidGenre_whenCallUpdateToInactive_thenReturnGenreUpdated() {
        final String expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var aGenre = Genre.newGenre("acao", true);

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var createdAt = aGenre.getCreatedAt();
        final var updatedAt = aGenre.getUpdatedAt();

        final var actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);

        assertDoesNotThrow(() -> aGenre.validate(new ThrowsValidationHandler()));

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertFalse(aGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(createdAt, actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(aGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = new ArrayList<>();

        final var actualGenre = Genre.newGenre("acao", expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(seriesID);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategories(expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOK() {

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategories(expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithNullList_shouldReceiveOK() {

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

}
