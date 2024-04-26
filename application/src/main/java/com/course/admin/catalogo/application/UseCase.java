package com.course.admin.catalogo.application;

import com.course.admin.catalogo.domain.category.Category;

public class UseCase {
    public Category execute() {
        return new Category();
    }
}