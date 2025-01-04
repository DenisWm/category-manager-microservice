package com.course.admin.catalogo.domain.category;

import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import com.course.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;
    private static final String NAME_SHOULD_NOT_BE_NULL = "'name' should not be null";
    private static final String NAME_SHOULD_NOT_BE_EMPTY = "'name' should not be empty";
    private static final String NAME_MUST_BE_BETWEEN_3_AND_255_CHARACTERS = "'name' must be between 3 and 255 characters";
    private final Category category;
    public CategoryValidator(final Category category, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = category;
    }

    private Category getCategory() {
        return category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        if(name == null) {
            getHandler().append(new Error(NAME_SHOULD_NOT_BE_NULL));
            return;
        }
        if(name.isBlank()) {
            getHandler().append(new Error(NAME_SHOULD_NOT_BE_EMPTY));
            return;
        }
        final int nameLength = name.trim().length();
        if(nameLength < NAME_MIN_LENGTH || nameLength > NAME_MAX_LENGTH) {
            getHandler().append(new Error(NAME_MUST_BE_BETWEEN_3_AND_255_CHARACTERS));
        }
    }
}
