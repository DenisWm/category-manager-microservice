package com.course.admin.catalogo.domain.category;

import com.course.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);
    void deleteById(CategoryID anId);
    Optional<Category> findById(CategoryID anId);
    Category update(Category aCategory);
    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
