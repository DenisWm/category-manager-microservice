package com.course.admin.catalogo.domain.genre;

import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.utils.IDUtils;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {

    protected final String value;

    private GenreID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }
    public static GenreID unique() {
        return GenreID.from(IDUtils.uuid());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GenreID genreID = (GenreID) o;

        return value.equals(genreID.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
