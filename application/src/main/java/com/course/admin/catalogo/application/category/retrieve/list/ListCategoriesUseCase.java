package com.course.admin.catalogo.application.category.retrieve.list;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import com.course.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
