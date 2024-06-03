package com.course.admin.catalogo.application.retrieve.get;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        save(aCategory);

        final var actualCategory = useCase.execute(expectedId.getValue());

        assertEquals(expectedId, actualCategory.id());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.isActive());
        System.out.println(aCategory.getCreatedAt());
        System.out.println(Date.from(aCategory.getCreatedAt()));
        assertTrue(DateUtils.truncatedEquals(Date.from(aCategory.getCreatedAt()), Date.from(actualCategory.createdAt()), Calendar.SECOND));
        assertTrue(DateUtils.truncatedEquals(Date.from(aCategory.getUpdatedAt()), Date.from(actualCategory.updatedAt()),Calendar.SECOND));
        assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var actualException = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).findById(eq(expectedId));

        final var actualException = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(Arrays.stream(aCategory).map(CategoryJpaEntity::from).toList());
    }
}
