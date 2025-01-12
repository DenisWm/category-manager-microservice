package com.course.admin.catalogo.application.video.retrieve.get;

import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.resource.Resource;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static com.course.admin.catalogo.application.utils.IDUtils.asSet;
import static com.course.admin.catalogo.application.utils.IDUtils.asString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidExistentId_whenGetVideoById_thenShouldReturnVideo() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of(
                Fixture.CastMembers.denis().getId(),
                Fixture.CastMembers.wesley().getId()
        );

        final var expectedVideo = audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = audioVideo(VideoMediaType.TRAILER);;
        final var expectedBanner = image(VideoMediaType.BANNER);
        final var expectedThumbNail = image(VideoMediaType.THUMBNAIL);
        final var expectedThumbNailHalf = image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbNail(expectedThumbNail)
                .setThumbNailHalf(expectedThumbNailHalf);

        final var anId = aVideo.getId();

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));

        final var actualVideo = useCase.execute(anId.getValue());

        assertEquals(anId.getValue(), actualVideo.id());

        assertEquals(expectedTitle, actualVideo.title());
        assertEquals(expectedDescription, actualVideo.description());
        assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        assertEquals(expectedDuration, actualVideo.duration());
        assertEquals(expectedOpened, actualVideo.opened());
        assertEquals(expectedPublished, actualVideo.published());
        assertEquals(expectedRating, actualVideo.rating());
        assertEquals(asSet(asString(expectedCategories)), actualVideo.categories());
        assertEquals(asSet(asString(expectedGenres)), actualVideo.genres());
        assertEquals(asSet(asString(expectedMembers)), actualVideo.castMembers());
        assertEquals(expectedVideo, actualVideo.video());
        assertEquals(expectedTrailer, actualVideo.trailer());
        assertEquals(expectedBanner, actualVideo.banner());
        assertEquals(expectedThumbNail, actualVideo.thumbnail());
        assertEquals(expectedThumbNailHalf, actualVideo.thumbnailHalf());
        assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
        assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());
    }

    @Test
    public void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        final var expectedErrorMessage = "Video with ID 123 was not found";
        final var expectedId = VideoID.from("123");

        when(videoGateway.findById(any())).thenReturn(Optional.empty());

        final var actualError = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualError.getMessage());

    }

    private AudioVideoMedia audioVideo(final VideoMediaType type) {
        final var checksum = IDUtils.uuid();
        return AudioVideoMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/videos/" + checksum,
                "",
                MediaStatus.PENDING
        );
    }

    private ImageMedia image(final VideoMediaType type) {
        final var checksum = IDUtils.uuid();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/images/" + checksum
        );
    }

}
