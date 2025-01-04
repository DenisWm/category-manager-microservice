package com.course.admin.catalogo.infrastructure.configuration.usecases;

import com.course.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.course.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.course.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import com.course.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.course.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.course.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.course.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.course.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.course.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.course.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfiguration {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfiguration(final GenreGateway genreGateway, final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }
}
