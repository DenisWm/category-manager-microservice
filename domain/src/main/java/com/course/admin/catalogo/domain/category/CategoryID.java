package com.course.admin.catalogo.domain.category;

import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.utils.IDUtils;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {

    protected final String value;

    private CategoryID(final String value) {
        this.value = Objects.requireNonNull(value);
    }
    public static CategoryID unique() {
        return CategoryID.from(IDUtils.uuid());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
