package com.course.admin.catalogo.application.category.retrieve.list;

import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import com.course.admin.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(SearchQuery aSearchQuery) {
        return this.categoryGateway.findAll(aSearchQuery)
                .map(CategoryListOutput::from);
    }
}
