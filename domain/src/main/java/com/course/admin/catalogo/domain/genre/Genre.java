package com.course.admin.catalogo.domain.genre;

import com.course.admin.catalogo.domain.AggregateRoot;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.category.CategoryValidator;
import com.course.admin.catalogo.domain.utils.InstantUtils;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import com.course.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.*;

public class Genre extends AggregateRoot<GenreID> {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreationAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = Objects.requireNonNull(aCreationAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    private void selfValidate() {
        final var notification = Notification.create();

        validate(notification);

        if(notification.hasErrors()) {
            throw new NotificationException("", notification);
        }
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var id = GenreID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(id, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(final GenreID anId,
                                final String name,
                                final boolean active,
                                final List<CategoryID> categories,
                                final Instant createdAt,
                                final Instant updatedAt,
                                final Instant deletedAt) {
        return new Genre(
                anId,
                name,
                active,
                categories,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Genre with(final Genre aGenre) {
        return with(
                aGenre.getId(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt());
    }

    public Genre update(final String aName,
                           final boolean isActive, final List<CategoryID> categories) {

        if(isActive) {
            this.activate();
        } else {
            this.deactivate();
        }
        this.name = aName;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.updatedAt = InstantUtils.now();

        selfValidate();

        return this;
    }
    public Genre deactivate() {
        Instant now = InstantUtils.now();
        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }
        this.active = false;
        this.updatedAt = now;
        return this;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new GenreValidator(this, aHandler).validate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCategories(List<CategoryID> categories) {
        this.categories = categories;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if(aCategoryID == null) {
            return this;
        }
        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(final List<CategoryID> categories) {
        if (categories == null || categories.isEmpty()) {
            return this;
        }
        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if(aCategoryID == null) {
            return this;
        }
        this.categories.remove(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }
}
