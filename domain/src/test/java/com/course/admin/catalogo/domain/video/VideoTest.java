package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.UnitTest;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.events.DomainEvent;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.utils.InstantUtils;
import com.course.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
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
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(actualVideo.getCreatedAt(), actualVideo.getUpdatedAt());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());
        assertTrue(actualVideo.getDomainEvents().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() throws Exception {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedEvent = new VideoMediaCreated("ID", "file");
        final var expectedEventCount = 1;

        final var aVideo = Video.newVideo(
                "Test title",
                "Lalala description",
                Year.of(2017),
                0.0,
                Rating.AGE_10,
                true,
                true,
                Set.of(),
                Set.of(),
                Set.of()
        );

        aVideo.registerEvent(expectedEvent);

        final var actualVideo = aVideo.with(aVideo).update(
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
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());
        assertEquals(expectedEventCount, actualVideo.getDomainEvents().size());
        assertEquals(expectedEvent, actualVideo.getDomainEvents().get(0));

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateVideoMedia_shouldReturnUpdated() throws Exception {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

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
        );
        final var aVideoMedia =
                AudioVideoMedia.with("abc", "Video.mp4", "/123/videos", "", MediaStatus.PENDING);

        final var expectedDomainEventsSize = 1;
        final var expectedDomainEvents = new VideoMediaCreated(aVideoMedia.id(), aVideoMedia.rawLocation());

        final var actualVideo = aVideo.with(aVideo).updateVideoMedia(aVideoMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertEquals(aVideoMedia, actualVideo.getVideo().get());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());
        assertEquals(expectedDomainEventsSize, actualVideo.getDomainEvents().size());
        final var actualEvent = (VideoMediaCreated) actualVideo.getDomainEvents().get(0);
        assertEquals(aVideo.getId().getValue(), actualEvent.resourceId());
        assertEquals(aVideoMedia.rawLocation(), actualEvent.filePath());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateTrailerMedia_shouldReturnUpdated() throws Exception {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

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
        );
        final var aTrailerMedia =
                AudioVideoMedia.with("abc", "Trailer.mp4", "/123/trailers", "", MediaStatus.PENDING);

        final var expectedDomainEventsSize = 1;

        final var actualVideo = aVideo.with(aVideo).updateTrailerMedia(aTrailerMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertEquals(aTrailerMedia, actualVideo.getTrailer().get());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());
        assertEquals(expectedDomainEventsSize, actualVideo.getDomainEvents().size());
        final var actualEvent = (VideoMediaCreated) actualVideo.getDomainEvents().get(0);
        assertEquals(aVideo.getId().getValue(), actualEvent.resourceId());
        assertEquals(aTrailerMedia.rawLocation(), actualEvent.filePath());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateBannerMedia_shouldReturnUpdated() throws Exception {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

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
        );
        final var aImageBannerMedia =
                ImageMedia.with("abc", "Banner.mp4", "/123/images");

        final var actualVideo = aVideo.with(aVideo).updateBannerMedia(aImageBannerMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertEquals(aImageBannerMedia, actualVideo.getBanner().get());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateThumbnailMedia_shouldReturnUpdated() throws Exception {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

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
        );
        final var aImageThumbNailMedia =
                ImageMedia.with("abc", "Thumbnail.mp4", "/123/images");

        final var actualVideo = aVideo.with(aVideo).updateThumbnailMedia(aImageThumbNailMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertEquals(aImageThumbNailMedia, actualVideo.getThumbNail().get());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateThumbnailHalfMedia_shouldReturnUpdated() throws Exception {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo =Video.newVideo(
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
        );
        final var aImageThumbNailHalfMedia =
                ImageMedia.with("abc", "Thumbnail-half.mp4", "/123/images");

        final var actualVideo = aVideo.with(aVideo).updateThumbnailHalfMedia(aImageThumbNailHalfMedia);

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertEquals(aImageThumbNailHalfMedia, actualVideo.getThumbNailHalf().get());
        assertTrue(actualVideo.getThumbNail().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsWith_shouldCreateWithoutEvents() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedEventCount = 0;



        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                null,
                null,
                null,
                null,
                null,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertEquals(expectedId, actualVideo.getId());
        assertEquals(expectedCreatedAt, actualVideo.getCreatedAt());
        assertEquals(expectedUpdatedAt, actualVideo.getUpdatedAt());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());
        assertEquals(expectedEventCount, actualVideo.getDomainEvents().size());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }
}
