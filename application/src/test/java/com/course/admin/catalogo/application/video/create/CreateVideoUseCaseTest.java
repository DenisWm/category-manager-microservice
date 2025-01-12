package com.course.admin.catalogo.application.video.create;

import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.InternalErrorException;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.resource.Resource;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static com.course.admin.catalogo.application.utils.IDUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateVideoUseCaseTest extends UseCaseTest {


    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(
                videoGateway,
                categoryGateway,
                genreGateway,
                castMemberGateway,
                mediaResourceGateway
        );
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
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
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbNail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbNailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(asList(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(asList(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(asList(expectedMembers));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumbNail.name(), actualVideo.getThumbNail().get().name())
                        && Objects.equals(expectedThumbNailHalf.name(), actualVideo.getThumbNailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateVideo_shouldReturnVideoId() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of(
                Fixture.CastMembers.denis().getId(),
                Fixture.CastMembers.wesley().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbNail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbNailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(genreGateway.existsByIds(any())).thenReturn(asList(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(asList(expectedMembers));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumbNail.name(), actualVideo.getThumbNail().get().name())
                        && Objects.equals(expectedThumbNailHalf.name(), actualVideo.getThumbNailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutGenres_whenCallsCreateVideo_shouldReturnVideoId() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of(
                Fixture.CastMembers.denis().getId(),
                Fixture.CastMembers.wesley().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbNail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbNailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(asList(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(asList(expectedMembers));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumbNail.name(), actualVideo.getThumbNail().get().name())
                        && Objects.equals(expectedThumbNailHalf.name(), actualVideo.getThumbNailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutMembers_whenCallsCreateVideo_shouldReturnVideoId() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbNail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbNailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(asList(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(asList(expectedGenres));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumbNail.name(), actualVideo.getThumbNail().get().name())
                        && Objects.equals(expectedThumbNailHalf.name(), actualVideo.getThumbNailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutResources_whenCallsCreateVideo_shouldReturnVideoId() {
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
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(asList(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(asList(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(asList(expectedMembers));
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.isOpened())
                        && Objects.equals(expectedPublished, actualVideo.isPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbNail().isEmpty()
                        && actualVideo.getThumbNailHalf().isEmpty()
        ));
    }

    @Test
    public void givenANullTitle_whenCallsCreateVideo_shouldReturnDomainException() {
        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAEmptyTitle_whenCallsCreateVideo_shouldReturnDomainException() {
        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenATitleWithMoreThan255Characters_whenCallsCreateVideo_shouldReturnDomainException() {
        final var expectedTitle = """
                    O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
                    O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
                    Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
                    É uma ótima prática para quem deseja dominar entrevistas de design.
                """;;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenANullRating_whenCallsCreateVideo_shouldReturnDomainException() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenANullLaunchedAt_whenCallsCreateVideo_shouldReturnDomainException() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchedAt = null;
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAnInvalidRating_whenCallsCreateVideo_shouldReturnDomainException() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = "INVALID";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var aulasId = Fixture.Categories.aulas().getId();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(aulasId);
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of(Fixture.CastMembers.denis().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(aulasId.getValue());
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>());
        when(genreGateway.existsByIds(any())).thenReturn(asList(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(asList(expectedMembers));
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {
        final var techId = Fixture.Genres.tech().getId();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of(techId);
        final var expectedMembers = Set.<CastMemberID>of(Fixture.CastMembers.denis().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(techId.getValue());
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(asList(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>());
        when(castMemberGateway.existsByIds(any())).thenReturn(asList(expectedMembers));
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeMembersDoesNotExists_shouldReturnDomainException() {
        final var denisId = Fixture.CastMembers.denis().getId();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of(denisId);
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbNail = null;
        final Resource expectedThumbNailHalf = null;

        final var expectedErrorMessage = "Some cast castMembers could not be found: %s".formatted(denisId.getValue());
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(asList(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(asList(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>());
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoThrowsException_shouldReturnCallClearResources() {
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
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbNail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbNailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var expectedErrorMessage = "An error on create video was observed [videoId:";

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asSet(asString(expectedCategories)),
                asSet(asString(expectedGenres)),
                asSet(asString(expectedMembers)),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbNail,
                expectedThumbNailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(asList(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(asList(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(asList(expectedMembers));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenThrow(new RuntimeException("Internal Server Error"));

        final var actualException = assertThrows(InternalErrorException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertTrue(actualException.getMessage().startsWith(expectedErrorMessage));

        verify(mediaResourceGateway).clearResources(any());
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenAnswer(t -> {
            final var resource = t.getArgument(1, VideoResource.class);
            return AudioVideoMedia.with(IDUtils.uuid(),  resource.resource().name(), "/img", "", MediaStatus.PENDING);
        });
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any())).thenAnswer(t -> {
            final var resource = t.getArgument(1, VideoResource.class);
            return ImageMedia.with(IDUtils.uuid(), resource.resource().name(), "/img");
        });
    }
}