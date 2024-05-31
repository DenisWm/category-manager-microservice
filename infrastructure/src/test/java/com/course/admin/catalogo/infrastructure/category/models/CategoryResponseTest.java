package com.course.admin.catalogo.infrastructure.category.models;

import com.course.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new CategoryResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );
        final var actualJson = json.write(response);
        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt)
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt)
        ;
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
                {
                    "id": "%s",
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s,
                    "created_at": "%s",
                    "updated_at": "%s",
                    "deleted_at": "%s"
                }
                """.formatted(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );
        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
        ;
    }


}
