package com.course.admin.catalogo.application.category.retrieve.get;

import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.DomainException;
import com.course.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(String anId) {
        final var aCategoryId = CategoryID.from(anId);
        return this.categoryGateway.findById(aCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(aCategoryId));
    }

    private static Supplier<DomainException> notFound(CategoryID aCategoryId) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not found".formatted(aCategoryId.getValue())));
    }
}
