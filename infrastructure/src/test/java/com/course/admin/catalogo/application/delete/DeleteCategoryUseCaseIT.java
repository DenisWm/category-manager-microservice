package com.course.admin.catalogo.application.delete;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() {

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        assertEquals(0, categoryRepository.count());

        save(aCategory);

        assertEquals(1, categoryRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        assertEquals(0, categoryRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).deleteById(expectedId);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).deleteById(expectedId);
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory).map(CategoryJpaEntity::from).toList()
        );
    }
}
