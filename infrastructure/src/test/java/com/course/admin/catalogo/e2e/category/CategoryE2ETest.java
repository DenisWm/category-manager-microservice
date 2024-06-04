package com.course.admin.catalogo.e2e.category;

import com.course.admin.catalogo.E2ETest;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.course.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.course.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.course.admin.catalogo.infrastructure.configuration.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MockMvc mvc;

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

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACategoryWithValidValuesByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());


        final var actualId = givenACategory("Movies", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        assertEquals(1, categoryRepository.count());

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aMockMvcRequestBuilder = put("/categories/{id}", actualId.getValue())
                .content(Json.writeValueAsString(requestBody))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(actualId.getValue())));

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }
    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualId = givenACategory(expectedName, expectedDescription, true);

        assertEquals(1, categoryRepository.count());

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aMockMvcRequestBuilder = put("/categories/{id}", actualId.getValue())
                .content(Json.writeValueAsString(requestBody))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(actualId.getValue())));

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, false);

        assertEquals(1, categoryRepository.count());

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aMockMvcRequestBuilder = put("/categories/{id}", actualId.getValue())
                .content(Json.writeValueAsString(requestBody))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(actualId.getValue())));

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedDescription = "A categoria mais assistida";

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        final var expectedErrorMessage = "Category with ID 123 was not found";

        assertEquals(0, categoryRepository.count());

        final var aMockMvcRequestBuilder = get("/categories/123")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON);

        this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACategoryAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        givenACategory("Filmes", null, true);
        givenACategory("Documentários", null, true);
        givenACategory("Séries", null, true);

        listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")));

        listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));

        listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Séries")));
        listCategories(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));

    }

    @Test
    public void asACategoryAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        givenACategory("Filmes", null, true);
        givenACategory("Documentários", null, true);
        givenACategory("Séries", null, true);

        listCategories(0, 1, "fIL")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));
    }

    @Test
    public void asACategoryAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        givenACategory("Filmes", "C", true);
        givenACategory("Documentários", "Z", true);
        givenACategory("Séries", "A", true);

        listCategories("", 0, 3, "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Filmes")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Séries")))
        ;

    }

    private ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(search, page, perPage, "", "");
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories("", page, perPage, "", "");
    }

    private ResultActions listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) throws Exception {

        final var aMockMvcRequestBuilder = get("/categories")
                .queryParam("search", search)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print());


        return response;
    }

    private CategoryResponse retrieveCategory(String anId) throws Exception {

        final var aMockMvcRequestBuilder = get("/categories/{id}", anId)
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

        final var aMockMvcRequestBuilder = post("/categories")
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

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var actualId = givenACategory("Filmes", "A categoria mais assistida", true);

        assertEquals(1, categoryRepository.count());

        final var aMockMvcRequestBuilder = delete("/categories/{id}", actualId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aMockMvcRequestBuilder)
                .andDo(print()).andExpect(status().isNoContent());

        assertFalse(this.categoryRepository.existsById(actualId.getValue()));
    }


}
