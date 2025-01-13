package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.AggregateRoot;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.utils.InstantUtils;
import com.course.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.*;

public class Video extends AggregateRoot<VideoID> {
    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbNail;
    private ImageMedia thumbNailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    protected Video(
            final VideoID anId,
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final Rating aRating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumbNail,
            final ImageMedia aThumbNailHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
            ) {
        super(anId);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedAt;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.banner = aBanner;
        this.thumbNail = aThumbNail;
        this.thumbNailHalf = aThumbNailHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final Rating aRating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        final var now = InstantUtils.now();
        final var anId = VideoID.unique();
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLaunchedAt,
                aDuration,
                aRating,
                wasOpened,
                wasPublished,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                castMembers
        );
    }

    public Video update(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final Rating aRating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedAt;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(castMembers);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public static Video with(
            final VideoID anId,
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final Rating aRating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumbNail,
            final ImageMedia aThumbNailHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLaunchedAt,
                aDuration,
                aRating,
                wasOpened,
                wasPublished,
                aCreationDate,
                aUpdateDate,
                aBanner,
                aThumbNail,
                aThumbNailHalf,
                aTrailer,
                aVideo,
                categories,
                genres,
                castMembers
        );
    }

    public static Video with(
            final Video aVideo
    ) {
        return new Video(
                aVideo.getId(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt(),
                aVideo.getDuration(),
                aVideo.getRating(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbNail().orElse(null),
                aVideo.getThumbNailHalf().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getVideo().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers())
        );
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbNail() {
        return Optional.ofNullable(thumbNail);
    }

    public Optional<ImageMedia> getThumbNailHalf() {
        return Optional.ofNullable(thumbNailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();

    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }


    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setThumbNail(final ImageMedia thumbNail) {
        this.thumbNail = thumbNail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setThumbNailHalf(final ImageMedia thumbNailHalf) {
        this.thumbNailHalf = thumbNailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    private Video setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
        return this;
    }

    private Video setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
        return this;
    }

    private Video setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
        return this;
    }

    public void processing(final VideoMediaType aType) {
        if(VideoMediaType.VIDEO == aType) {
            getVideo().ifPresent(media -> setVideo(media.processing()));
        } else

        if(VideoMediaType.TRAILER == aType) {
            getTrailer().ifPresent(media -> setTrailer(media.processing()));
        }
    }

    public void completed(final VideoMediaType aType, final String encodedPath) {
        if(VideoMediaType.VIDEO == aType) {
            getVideo().ifPresent(media -> setVideo(media.completed(encodedPath)));
        } else

        if(VideoMediaType.TRAILER == aType) {
            getTrailer().ifPresent(media -> setTrailer(media.completed(encodedPath)));
        }
    }
}
