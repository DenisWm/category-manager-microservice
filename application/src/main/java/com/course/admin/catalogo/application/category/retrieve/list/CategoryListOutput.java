package com.course.admin.catalogo.application.category.retrieve.list;

import com.course.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;
import java.util.List;

public record CategoryListOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {

    public static CategoryListOutput from(final Category anCategory) {
        return new CategoryListOutput(anCategory.getId(),
                anCategory.getName(),
                anCategory.getDescription(),
                anCategory.isActive(),
                anCategory.getCreatedAt(),
                anCategory.getDeletedAt()
        );
    }
}
