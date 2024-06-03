package com.course.admin.catalogo.e2e.category;

import com.course.admin.catalogo.E2ETest;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.course.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.course.admin.catalogo.infrastructure.configuration.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mvc;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedDescription = "A categoria mais assistida";

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(1, categoryRepository.count());

        final var actualCategory = retrieveCategory(actualId.getValue());

        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.active());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());

    }

    private CategoryResponse retrieveCategory(String anId) throws Exception {

        final var aMockMvcRequestBuilder = MockMvcRequestBuilders.get("/categories/{id}", anId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print());

        final var jsonContent = response.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(jsonContent, CategoryResponse.class);
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

        final var aMockMvcRequestBuilder = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var response = this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print());

        final var actualId = response.andExpect(status().isCreated())
                        .andReturn()
                        .getResponse().getHeader("Location")
                        .replace("/categories/", "");

        return CategoryID.from(actualId);
    }


}
