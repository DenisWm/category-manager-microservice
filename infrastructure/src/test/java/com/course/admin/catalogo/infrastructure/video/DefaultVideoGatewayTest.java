package com.course.admin.catalogo.infrastructure.video;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.resource.Resource;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.video.*;
import com.course.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.wildfly.common.Assert.assertFalse;
import static org.wildfly.common.Assert.assertTrue;

@IntegrationTest
class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    private CastMember wesley;

    private CastMember denis;

    private Category aulas;

    private Category lives;

    private Genre tech;

    private Genre business;

    @BeforeEach
    public void setUp() {
        this.wesley = castMemberGateway.create(Fixture.CastMembers.wesley());
        this.denis = castMemberGateway.create(Fixture.CastMembers.denis());
        this.aulas = categoryGateway.create(Fixture.Categories.aulas());
        this.lives = categoryGateway.create(Fixture.Categories.lives());
        this.tech = genreGateway.create(Fixture.Genres.tech());
        this.business = genreGateway.create(Fixture.Genres.business());
    }


    @Test
    void testInjection() {
        assertNotNull(videoGateway);
        assertNotNull(castMemberGateway);
        assertNotNull(categoryGateway);
        assertNotNull(genreGateway);
        assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.<GenreID>of(tech.getId());
        final var expectedMembers = Set.<CastMemberID>of(
                wesley.getId()
        );

        final var expectedVideo = audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = audioVideo(VideoMediaType.TRAILER);
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


        final var actualVideo = videoGateway.create(
                aVideo
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());


        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumbNail.name(), actualVideo.getThumbNail().get().name());
        assertEquals(expectedThumbNailHalf.name(), actualVideo.getThumbNailHalf().get().name());

        final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchedAt, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenreID());
        assertEquals(expectedMembers, persistedVideo.getCastMemberID());
        assertEquals(expectedVideo.name(), persistedVideo.getVideo().toDomain().name());
        assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().toDomain().name());
        assertEquals(expectedBanner.name(), persistedVideo.getBanner().toDomain().name());
        assertEquals(expectedThumbNail.name(), persistedVideo.getThumbnail().toDomain().name());
        assertEquals(expectedThumbNailHalf.name(), persistedVideo.getThumbnailHalf().toDomain().name());
    }

    @Test
    @Transactional
    void givenAValidVideoWithoutRelations_whenCallsCreate_shouldPersistIt() {

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

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


        final var actualVideo = videoGateway.create(
                aVideo
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());


        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());

        final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchedAt, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenreID());
        assertEquals(expectedMembers, persistedVideo.getCastMemberID());
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    void givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
        final var video = videoGateway.create(
                Video.newVideo(
                        Fixture.title(),
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(),
                        Set.<GenreID>of(),
                        Set.<CastMemberID>of()
                )
        );

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.<GenreID>of(tech.getId());
        final var expectedMembers = Set.<CastMemberID>of(
                wesley.getId()
        );

        final var expectedVideo = audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = image(VideoMediaType.BANNER);
        final var expectedThumbNail = image(VideoMediaType.THUMBNAIL);
        final var expectedThumbNailHalf = image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.with(video).update(
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


        final var actualVideo = videoGateway.update(
                aVideo
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());


        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumbNail.name(), actualVideo.getThumbNail().get().name());
        assertEquals(expectedThumbNailHalf.name(), actualVideo.getThumbNailHalf().get().name());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(actualVideo.getUpdatedAt().isAfter(video.getUpdatedAt()));

        final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchedAt, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenreID());
        assertEquals(expectedMembers, persistedVideo.getCastMemberID());
        assertEquals(expectedVideo.name(), persistedVideo.getVideo().toDomain().name());
        assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().toDomain().name());
        assertEquals(expectedBanner.name(), persistedVideo.getBanner().toDomain().name());
        assertEquals(expectedThumbNail.name(), persistedVideo.getThumbnail().toDomain().name());
        assertEquals(expectedThumbNailHalf.name(), persistedVideo.getThumbnailHalf().toDomain().name());
    }

    @Test
    void givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        final var video = videoGateway.create(
                Video.newVideo(
                        Fixture.title(),
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(),
                        Set.<GenreID>of(),
                        Set.<CastMemberID>of()
                )
        );

        final var anId = video.getId();

        assertEquals(1, videoRepository.count());

        videoGateway.deleteById(
                anId
        );

        assertEquals(0, videoRepository.count());

    }

    @Test
    void givenAInvalidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        videoGateway.create(
                Video.newVideo(
                        Fixture.title(),
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(),
                        Set.<GenreID>of(),
                        Set.<CastMemberID>of()
                )
        );

        final var anId = VideoID.unique();

        assertEquals(1, videoRepository.count());

        videoGateway.deleteById(
                anId
        );

        assertEquals(1, videoRepository.count());

    }

    @Test
    void givenAValidVideo_whenCallsFindById_shouldReturnIt() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.<GenreID>of(tech.getId());
        final var expectedMembers = Set.<CastMemberID>of(
                wesley.getId()
        );

        final var expectedVideo = audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = image(VideoMediaType.BANNER);
        final var expectedThumbNail = image(VideoMediaType.THUMBNAIL);
        final var expectedThumbNailHalf = image(VideoMediaType.THUMBNAIL_HALF);


        final var aVideo = videoGateway.create(Video.newVideo(
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
                .setThumbNailHalf(expectedThumbNailHalf)
        );

        final var anId = aVideo.getId();

        final var actualVideo = videoGateway.findById(
                anId
        ).get();

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumbNail.name(), actualVideo.getThumbNail().get().name());
        assertEquals(expectedThumbNailHalf.name(), actualVideo.getThumbNailHalf().get().name());
    }

    @Test
    void givenAnInvalidValidVideoId_whenCallsFindById_shouldReturnEmpty() {
        videoGateway.create(
                Video.newVideo(
                        Fixture.title(),
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(),
                        Set.<GenreID>of(),
                        Set.<CastMemberID>of()
                )
        );

        final var anId = VideoID.unique();

        final var actualVideo = videoGateway.findById(
                anId
        );

        assertFalse(actualVideo.isPresent());
    }

    @Test
    void givenAValidCategory_whenCallsFindAll_shouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        Set.of(aulas.getId()),
                        null
                );

        final var actualPage = videoGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        assertEquals("Aula de empreendedorismo", actualPage.items().get(1).title());
    }

    @Test
    void givenAValidCastMember_whenCallsFindAll_shouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(wesley.getId()),
                        null,
                        null
                );

        final var actualPage = videoGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
        assertEquals("System Design", actualPage.items().get(1).title());
    }

    @Test
    void givenAValidGenre_whenCallsFindAll_shouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        Set.of(business.getId())
                );

        final var actualPage = videoGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    }

    @Test
    void givenAllParams_whenCallsFindAll_shouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "empreendedorismo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(wesley.getId()),
                        Set.of(aulas.getId()),
                        Set.of(business.getId())
                );

        final var actualPage = videoGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    }

    @Test
    void givenEmptyParams_whenCallsFindAll_shouldReturnAllList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

        final var actualPage = videoGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenEmptyVideos_whenCallsFindAll_shouldReturnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

        final var actualPage = videoGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,4,4,21.1 Implementação dos testes integrados do findAll",
            "title,desc,0,10,4,4,System Design",
            "createdAt,asc,0,10,4,4,System Design",
            "createdAt,desc,0,10,4,4,Aula de empreendedorismo",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideoTitle
    ) {
        // given
        final var expectedTerms = "";

        mockVideos();

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedVideoTitle, actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "yste,0,10,1,1,System Design",
            "eta ,0,10,1,1,Não cometa esse erro",
            "stes inte,0,10,1,1,21.1 Implementação dos testes integrados do findAll",
            "e empree,0,10,1,1,Aula de empreendedorismo"
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideoTitle
    ) {

        mockVideos();
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

        final var actualPage = videoGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedVideoTitle, actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,4,21.1 Implementação dos testes integrados do findAll;Aula de empreendedorismo",
            "1,2,2,4,Não cometa esse erro;System Design",
            "0,4,4,4,21.1 Implementação dos testes integrados do findAll;" +
                    "Aula de empreendedorismo;" +
                    "Não cometa esse erro;" +
                    "System Design"
    })
    public void givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideos
    ) {
        // given
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        mockVideos();

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        null,
                        null,
                        null
                );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedTitle : expectedVideos.split(";")) {
            final var actualTitle = actualPage.items().get(index).title();
            Assertions.assertEquals(expectedTitle, actualTitle);
            index++;
        }
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

    private void mockVideos() {
        videoGateway.create(
                Video.newVideo(
                        "System Design",
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(lives.getId()),
                        Set.<GenreID>of(tech.getId()),
                        Set.<CastMemberID>of(wesley.getId(), denis.getId())
                )
        );

        videoGateway.create(
                Video.newVideo(
                        "Não cometa esse erro",
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(),
                        Set.<GenreID>of(),
                        Set.<CastMemberID>of()
                )
        );

        videoGateway.create(
                Video.newVideo(
                        "21.1 Implementação dos testes integrados do findAll",
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(aulas.getId()),
                        Set.<GenreID>of(tech.getId()),
                        Set.<CastMemberID>of(denis.getId())
                )
        );

        videoGateway.create(
                Video.newVideo(
                        "Aula de empreendedorismo",
                        Fixture.Videos.description(),
                        Year.of(Fixture.year()),
                        Fixture.duration(),
                        Fixture.Videos.rating(),
                        Fixture.bool(),
                        Fixture.bool(),
                        Set.<CategoryID>of(aulas.getId()),
                        Set.<GenreID>of(business.getId()),
                        Set.<CastMemberID>of(wesley.getId())
                )
        );
    }
}