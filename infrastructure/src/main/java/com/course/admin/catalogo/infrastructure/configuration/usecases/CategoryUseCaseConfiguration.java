package com.course.admin.catalogo.infrastructure.configuration.usecases;

import com.course.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.course.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.course.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.course.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.course.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.course.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.course.admin.catalogo.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.course.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.course.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.course.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfiguration {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfiguration(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }
}
