package com.course.admin.catalogo.domain.genre;

import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import com.course.admin.catalogo.domain.validation.Validator;

public class GenreValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 1;
    public static final String NAME_SHOULD_NOT_BE_NULL = "'name' should not be null";
    public static final String NAME_SHOULD_NOT_BE_EMPTY = "'name' should not be empty";
    public static final String NAME_MUST_BE_BETWEEN_1_AND_255_CHARACTERS = "'name' must be between 1 and 255 characters";
    private final Genre genre;
    public GenreValidator(Genre genre, ValidationHandler aHandler) {
        super(aHandler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.genre.getName();
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
            getHandler().append(new Error(NAME_MUST_BE_BETWEEN_1_AND_255_CHARACTERS));
        }
    }
}
