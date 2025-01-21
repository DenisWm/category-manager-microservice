package com.course.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record UpdateVideoRequest(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("duration") Double duration,
        @JsonProperty("year_launched") Integer launchedAt,
        @JsonProperty("opened") Boolean opened,
        @JsonProperty("published") Boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("genres") Set<String> genres,
        @JsonProperty("cast_members") Set<String> members
) {

    public static UpdateVideoRequest with(final String title,
                                          final String description,
                                          final Double duration,
                                          final Integer launchedAt,
                                          final Boolean opened,
                                          final Boolean published,
                                          final String rating,
                                          final Set<String> categories,
                                          final Set<String> genres,
                                          final Set<String> members) {
        return new UpdateVideoRequest(
                title,
                description,
                duration,
                launchedAt,
                opened,
                published,
                rating,
                categories,
                genres,
                members
        );
    }
}
