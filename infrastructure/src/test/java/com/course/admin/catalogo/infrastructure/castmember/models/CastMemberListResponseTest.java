package com.course.admin.catalogo.infrastructure.castmember.models;

import com.course.admin.catalogo.JacksonTest;
import com.course.admin.catalogo.domain.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CastMemberListResponseTest {

    @Autowired
    private JacksonTester<CastMemberListResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type().name();
        final var expectedCreatedAt = Instant.now().toString();

        final var response = new CastMemberListResponse(
                expectedId,
                expectedName,
                expectedType,
                expectedCreatedAt
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt);
    }
}