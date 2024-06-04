package com.course.admin.catalogo.infrastructure.api.controllers;

import com.course.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.course.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.course.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.course.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.course.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.course.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.course.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.course.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.course.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.course.admin.catalogo.domain.category.CategorySearchQuery;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.validation.handler.Notification;
import com.course.admin.catalogo.infrastructure.api.CategoryAPI;
import com.course.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.course.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.course.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.course.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.course.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {

        final var aQuery = new CategorySearchQuery(page, perPage, search, sort, direction);
        return listCategoriesUseCase.execute(aQuery).map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateCategoryById(final String id, final UpdateCategoryRequest input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public void deleteCategoryById(final String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
