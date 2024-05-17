package com.course.admin.catalogo.infrastructure.category;

import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.category.CategorySearchQuery;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anId) {
        final String anIdValue = anId.getValue();
        if(categoryRepository.existsById(anIdValue)) {
            categoryRepository.deleteById(anIdValue);
        }

    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return categoryRepository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }

    private Category save(final Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
