package com.course.admin.catalogo.infrastructure.video;

import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.video.*;
import com.course.admin.catalogo.infrastructure.utils.SqlUtils;
import com.course.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import com.course.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository repository;

    public DefaultVideoGateway(final VideoRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }

    private Video save(final Video aVideo) {
        return repository.save(VideoJpaEntity.from(aVideo)).toAggregate();
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return repository.save(VideoJpaEntity.from(aVideo)).toAggregate();
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var videoId = anId.getValue();
        if(this.repository.existsById(videoId)) {
            this.repository.deleteById(videoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anId) {
        return repository.findById(anId.getValue()).map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.repository.findAll(
                SqlUtils.like(aQuery.terms()),
                toString(aQuery.castMember()),
                toString(aQuery.categories()),
                toString(aQuery.genres()),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Set<String> toString(final Set<? extends Identifier> ids) {
        if(ids == null || ids.isEmpty()) {
            return null;
        }
        return ids.stream().map(Identifier::toString).collect(Collectors.toSet());
    }
}
