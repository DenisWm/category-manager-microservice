package com.course.admin.catalogo.infrastructure.genre;

import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import com.course.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.course.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.course.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(final GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre).toAggregate();
    }

    @Override
    public void deleteById(final GenreID anId) {
        final var aGenreId = anId.getValue();
        if(this.genreRepository.existsById(aGenreId)) {
            genreRepository.deleteById(aGenreId);
        }
    }
    @Override
    public Optional<Genre> findById(final GenreID anId) {
        return this.genreRepository.findById(anId.getValue()).map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(Genre aGenre) {
        return save(aGenre).toAggregate();

    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var results = this.genreRepository.findAll(Specification.where(where), page);
        return new Pagination<>(
                results.getNumber(),
                results.getSize(),
                results.getTotalElements(),
                results.map(GenreJpaEntity::toAggregate).stream().toList()
        );
    }

    @Override
    public List<GenreID> existsByIds(final Iterable<GenreID> genreIds) {
        final var ids = StreamSupport.stream(genreIds.spliterator(), false)
                .map(GenreID::getValue)
                .toList();
        return this.genreRepository.existsByIds(ids).stream()
                .map(GenreID::from)
                .toList();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    private GenreJpaEntity save(Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre));
    }
}
