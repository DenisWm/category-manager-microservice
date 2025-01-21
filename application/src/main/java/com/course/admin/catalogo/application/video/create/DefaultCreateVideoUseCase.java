package com.course.admin.catalogo.application.video.create;

import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.InternalErrorException;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import com.course.admin.catalogo.domain.validation.handler.Notification;
import com.course.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase
        extends CreateVideoUseCase {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultCreateVideoUseCase(
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }


    @Override
    public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
        final var aRating = Rating.of(aCommand.rating()).orElse(null);
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.members(), CastMemberID::from);
        final var aTitle = aCommand.title();
        final var aDescription = aCommand.description();
        final var aLaunchedAt = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final var aDuration = aCommand.duration();
        final var isOpened = aCommand.opened();
        final var isPublished = aCommand.published();

        final var notification = Notification.create();

        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateCastMembers(members));

        final var aVideo = Video.newVideo(aTitle, aDescription, aLaunchedAt, aDuration, aRating, isOpened, isPublished, categories, genres, members);

        aVideo.validate(notification);

        if(notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Video", notification);
        }

        return CreateVideoOutput.from(create(aCommand, aVideo));
    }

    private Video create(final CreateVideoCommand aCommand, final Video aVideo) {
        final var anId = aVideo.getId();

        try {
            final var aVideoMedia = aCommand.getVideo()
                    .map(it -> mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, VideoMediaType.VIDEO)))
                    .orElse(null);

            final var aTrailerMedia = aCommand.getTrailer()
                    .map(it -> mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, VideoMediaType.TRAILER)))
                    .orElse(null);

            final var aBannerMedia = aCommand.getBanner()
                    .map(it -> mediaResourceGateway.storeImage(anId, VideoResource.with(it, VideoMediaType.BANNER)))
                    .orElse(null);

            final var aThumbNailMedia = aCommand.getThumbNail()
                    .map(it -> mediaResourceGateway.storeImage(anId, VideoResource.with(it, VideoMediaType.THUMBNAIL)))
                    .orElse(null);

            final var aThumbNailHalfMedia = aCommand.getThumbNailHalf()
                    .map(it -> mediaResourceGateway.storeImage(anId, VideoResource.with(it, VideoMediaType.THUMBNAIL_HALF)))
                    .orElse(null);


            return videoGateway.create(
                    aVideo
                            .updateVideoMedia(aVideoMedia)
                            .updateTrailerMedia(aTrailerMedia)
                            .updateBannerMedia(aBannerMedia)
                            .updateThumbnailMedia(aThumbNailMedia)
                            .updateThumbnailHalfMedia(aThumbNailHalfMedia)
            );
        } catch (Throwable t) {
            mediaResourceGateway.clearResources(anId);
            throw InternalErrorException.with("An error on create video was observed [videoId:%s]".formatted(anId.getValue()), t);
        }
    }

    private ValidationHandler validateCategories(final Set<CategoryID> categories) {
        return validateAggregate("categories", categories, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> genres) {
        return validateAggregate("genres", genres, genreGateway::existsByIds);
    }

    private ValidationHandler validateCastMembers(final Set<CastMemberID> members) {
        return validateAggregate("cast castMembers", members, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregateName,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(T::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregateName, missingIdsMessage)));
        }
        return notification;
    }


    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }

}
