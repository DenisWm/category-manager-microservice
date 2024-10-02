package com.course.admin.catalogo.e2e;

import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.course.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.course.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.course.admin.catalogo.infrastructure.configuration.Json;
import com.course.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.course.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return this.delete("/categories/", anId);
    }

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);
        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(search, page, perPage, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories("", page, perPage, "", "");
    }

    default ResultActions listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) throws Exception {

        return list("/categories", search, page, perPage, sort, direction);
    }
    default ResultActions deleteAGenre(final Identifier anId) throws Exception {
        return this.delete("/genres/", anId);
    }

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);
        return GenreID.from(actualId);
    }
    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(search, page, perPage, "", "");
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres("", page, perPage, "", "");
    }

    default ResultActions listGenres(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) throws Exception {

        return list("/genres", search, page, perPage, sort, direction);
    }

    default CategoryResponse retrieveCategory(Identifier anId) throws Exception {
        return retrieve("/categories/", CategoryResponse.class, anId);
    }

    default ResultActions updateCategory(Identifier anId, final UpdateCategoryRequest aRequest) throws Exception {
        return update("/categories/", aRequest, anId);
    }

    default ResultActions updateGenre(Identifier anId, final UpdateGenreRequest aRequest) throws Exception {
        return update("/genres/", aRequest, anId);
    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }

    private <T> T retrieve(String url, Class<T> clazz,  final Identifier anId) throws Exception {

        final var aMockMvcRequestBuilder = get( url + anId.getValue())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON);

        final var response = this.mvc().perform(aMockMvcRequestBuilder)
                .andDo(print());

        final var jsonContent = response.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(jsonContent, clazz);
    }

    private ResultActions update(String url,  final Object aRequestBody, final Identifier id) throws Exception {
        final var aRequest = put(url + id.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);

    }

    private ResultActions delete(String url,  final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue()).contentType(MediaType.APPLICATION_JSON);
        return this.mvc().perform(aRequest);
    }

    private ResultActions list(
            final String url,
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) throws Exception {

        final var aMockMvcRequestBuilder = get(url)
                .queryParam("search", search)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc().perform(aMockMvcRequestBuilder)
                .andDo(print());

        return response;
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var response = this.mvc().perform(aRequest)
                .andDo(print());

        final var actualId = response.andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");

        return actualId;
    }
}
