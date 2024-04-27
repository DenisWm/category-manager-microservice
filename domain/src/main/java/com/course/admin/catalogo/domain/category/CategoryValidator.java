package com.course.admin.catalogo.domain.category;

import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import com.course.admin.catalogo.domain.validation.Validator;

import java.util.List;

public class CategoryValidator extends Validator {
    private final Category category;
    private final ValidationHandler validationHandler;
    public CategoryValidator(final Category category, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = category;
        this.validationHandler = aHandler;

    }


    private Category getCategory() {
        return category;
    }


    @Override
    public void validate() {
        if(this.category.getName() == null) {
            this.validationHandler.append(new Error("'name' should not be null"));
        }
    }
}
