package com.course.admin.catalogo.application;

import com.course.admin.catalogo.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIN);
}