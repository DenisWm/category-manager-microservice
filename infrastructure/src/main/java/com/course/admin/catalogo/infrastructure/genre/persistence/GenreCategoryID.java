package com.course.admin.catalogo.infrastructure.genre.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {
    @Column(name = "genre_id", nullable = false)
    private String genreId;
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    public GenreCategoryID() {
    }

    private GenreCategoryID(final String aGenreId, final String aCategoryId) {
        this.genreId = aGenreId;
        this.categoryId = aCategoryId;
    }

    public static GenreCategoryID from(final String aGenreId, final String aCategoryId) {
        return new GenreCategoryID(aGenreId, aCategoryId);
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GenreCategoryID that = (GenreCategoryID) o;

        if (!Objects.equals(genreId, that.genreId)) return false;
        return Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        int result = genreId != null ? genreId.hashCode() : 0;
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        return result;
    }
}
