package com.course.admin.catalogo.domain.castmember;

import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import com.course.admin.catalogo.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 1;
    private static final String NAME_SHOULD_NOT_BE_NULL = "'name' should not be null";
    private static final String NAME_SHOULD_NOT_BE_EMPTY = "'name' should not be empty";
    private static final String TYPE_SHOULD_NOT_BE_EMPTY = "'type' should not be null";
    private static final String NAME_MUST_BE_BETWEEN_1_AND_255_CHARACTERS = "'name' must be between 1 and 255 characters";

    private final CastMember castMember;
    public CastMemberValidator(ValidationHandler handler, CastMember castMember) {
        super(handler);
        this.castMember = castMember;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();

    }

    private void checkTypeConstraints() {
        final var type = this.castMember.getType();
        if(type == null) {
            getHandler().append(new Error(TYPE_SHOULD_NOT_BE_EMPTY));
        }
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();
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
