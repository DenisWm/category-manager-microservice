package com.course.admin.catalogo.application.category.create;

import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = Category.newCategory(aName, aDescription, isActive);
        aCategory.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));
    }
}
