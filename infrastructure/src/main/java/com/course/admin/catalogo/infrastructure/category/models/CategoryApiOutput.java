package com.course.admin.catalogo.infrastructure.category.models;

import com.course.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CategoryApiOutput(
                                @JsonProperty(value = "id") String id,
                                @JsonProperty(value = "name") String name,
                                @JsonProperty(value = "description") String description,
                                @JsonProperty(value = "is_active") Boolean active,
                                @JsonProperty(value = "created_at") Instant createdAt,
                                @JsonProperty(value = "updated_at") Instant updatedAt,
                                @JsonProperty(value = "deleted_at") Instant deletedAt
) {

    public static CategoryApiOutput from(final CategoryOutput anOutput) {
        return new CategoryApiOutput(
                anOutput.id().getValue(),
                anOutput.name(),
                anOutput.description(),
                anOutput.isActive(),
                anOutput.createdAt(),
                anOutput.updatedAt(),
                anOutput.deletedAt() != null ? anOutput.deletedAt() : null
        );
    }
}
