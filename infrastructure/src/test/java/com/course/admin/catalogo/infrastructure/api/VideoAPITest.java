package com.course.admin.catalogo.infrastructure.api;

import com.course.admin.catalogo.ApiTest;
import com.course.admin.catalogo.ControllerTest;
import com.course.admin.catalogo.application.video.create.CreateVideoCommand;
import com.course.admin.catalogo.application.video.create.CreateVideoOutput;
import com.course.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.course.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.course.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.course.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.course.admin.catalogo.application.video.media.get.MediaOutput;
import com.course.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.course.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.course.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.course.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.course.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.course.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.course.admin.catalogo.application.video.retrieve.list.VideoListOutput;
import com.course.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.course.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.course.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.video.*;
import com.course.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import com.course.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.course.admin.catalogo.domain.utils.CollectionUtils.mapTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private ListVideoUseCase listVideoUseCase;

    @MockBean
    private GetMediaUseCase getMediaUseCase;

    @MockBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        final var wesley = Fixture.CastMembers.wesley();

        final var aulas = Fixture.Categories.aulas();

        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(
                wesley.getId().getValue()
        );
        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());
        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());
        final var expectedThumbNail =
                new MockMultipartFile("thumbnail_file", "thumbnail.jpg", "image/jpg", "THUMB_NAIL".getBytes());
        final var expectedThumbNailHalf =
                new MockMultipartFile("thumbnail_half_file", "thumbnail_half.jpg", "image/jpg", "THUMBNAIL_HALF".getBytes());

        when(createVideoUseCase.execute(any())).thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumbNail)
                .file(expectedThumbNailHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchedAt.getValue()))
                .param("duration", expectedDuration.toString())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", wesley.getId().getValue())
                .param("categories_id", aulas.getId().getValue())
                .param("genres_id", tech.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(ApiTest.VIDEOS_JWT)
                ;

        this.mvc.perform(aRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
        ;

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchedAt.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertEquals(expectedVideo.getOriginalFilename(), actualCmd.getVideo().get().name());
        assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.getTrailer().get().name());
        assertEquals(expectedBanner.getOriginalFilename(), actualCmd.getBanner().get().name());
        assertEquals(expectedThumbNail.getOriginalFilename(), actualCmd.getThumbNail().get().name());
        assertEquals(expectedThumbNailHalf.getOriginalFilename(), actualCmd.getThumbNailHalf().get().name());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateFull_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";
        when(createVideoUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = multipart("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
        ;

    }

    @Test
    public void givenAValidCommand_whenCallsCreatePartial_shouldReturnAnId() throws Exception {
        final var wesley = Fixture.CastMembers.wesley();

        final var aulas = Fixture.Categories.aulas();

        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(
                wesley.getId().getValue()
        );

        final var aCommand = CreateVideoRequest.with(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchedAt.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        when(createVideoUseCase.execute(any())).thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .content(objectMapper.writeValueAsBytes(aCommand))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
        ;

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchedAt.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbNail().isEmpty());
        assertTrue(actualCmd.getThumbNailHalf().isEmpty());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";


        when(createVideoUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                  "title":"title"
                                }
                                """
                );

        this.mvc.perform(aRequest)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
        ;

    }

    @Test
    public void givenAnEmptyCommand_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var aRequest = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        this.mvc.perform(aRequest)
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        final var wesley = Fixture.CastMembers.wesley();

        final var aulas = Fixture.Categories.aulas();

        final var tech = Fixture.Genres.tech();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(
                wesley.getId().getValue()
        );

        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumbnail = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbnailHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        mapTo(expectedCategories, CategoryID::from),
                        mapTo(expectedGenres, GenreID::from),
                        mapTo(expectedMembers, CastMemberID::from)
                ).updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumbnail)
                .updateThumbnailHalfMedia(expectedThumbnailHalf);

        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any())).thenReturn(VideoOutput.from(aVideo));

        final var aRequest = get("/videos/{id}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest).andDo(print());

        aResponse.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchedAt.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumbnail.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumbnail.name())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumbnail.checksum())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumbnail.location())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbnailHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbnailHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbnailHalf.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbnailHalf.location())))
                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList(expectedMembers))));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        final var expectedId = VideoID.unique();

        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(getVideoByIdUseCase.execute(any())).thenThrow(NotFoundException.with(Video.class, expectedId));

        final var aRequest = get("/videos/{id}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest).andDo(print());

        aResponse.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
        final var wesley = Fixture.CastMembers.wesley();

        final var aulas = Fixture.Categories.aulas();

        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(
                wesley.getId().getValue()
        );

        final var aCommand = UpdateVideoRequest.with(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchedAt.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        when(updateVideoUseCase.execute(any())).thenReturn(new UpdateVideoOutput(expectedId.getValue()));

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .content(objectMapper.writeValueAsBytes(aCommand))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
        ;

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateVideoCommand.class);

        verify(updateVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchedAt.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbNail().isEmpty());
        assertTrue(actualCmd.getThumbNailHalf().isEmpty());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
        final var wesley = Fixture.CastMembers.wesley();

        final var aulas = Fixture.Categories.aulas();

        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(
                wesley.getId().getValue()
        );

        final var aCommand = UpdateVideoRequest.with(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchedAt.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        when(updateVideoUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .content(objectMapper.writeValueAsBytes(aCommand))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(print());

        aResponse
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)))
        ;

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        final var expectedId = VideoID.unique();

        doNothing().when(deleteVideoUseCase).execute(any());

        final var aRequest = delete("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isNoContent());

        verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
        final var aVideo = VideoPreview.from(Fixture.video());

        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCategories = "cat1";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideoUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var aRequest = get("/videos")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedCastMembers)
                .queryParam("genres_ids", expectedGenres)
                .queryParam("categories_ids", expectedCategories)
                .accept(MediaType.APPLICATION_JSON)
                .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideoUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();

        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedDirection, actualQuery.direction());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedTerms, actualQuery.terms());
        assertEquals(Set.of(CategoryID.from(expectedCategories)), actualQuery.categories());
        assertEquals(Set.of(GenreID.from(expectedGenres)), actualQuery.genres());
        assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), actualQuery.castMember());
    }


    @Test
    public void givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() throws Exception {
        final var aVideo = VideoPreview.from(Fixture.video());

        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideoUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var aRequest = get("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideoUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();

        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedDirection, actualQuery.direction());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedTerms, actualQuery.terms());
        assertTrue(actualQuery.categories().isEmpty());
        assertTrue(actualQuery.genres().isEmpty());
        assertTrue(actualQuery.castMember().isEmpty());
    }

    @Test
    public void givenAValidVideoIdAndFileType_whenCallsGetMediaById_ShouldReturnContent() throws Exception {
        final var expectedId = VideoID.unique();

        final var expectedMediaType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedMediaType);

        final var expectedMedia = new MediaOutput(
                expectedResource.content(),
                expectedResource.contentType(),
                expectedResource.name()
        );

        when(getMediaUseCase.execute(any())).thenReturn(expectedMedia);

        final var aRequest = get("/videos/{id}/medias/{type}", expectedId.getValue(), expectedMediaType.name())
                .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(header().string(CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(header().string(CONTENT_DISPOSITION, "attachment; filename=" + expectedMedia.name()))
                .andExpect(content().bytes(expectedMedia.content()));

        final var captor = ArgumentCaptor.forClass(GetMediaCommand.class);

        verify(this.getMediaUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();

        assertEquals(expectedId.getValue(), actualCmd.videoId());
        assertEquals(expectedMediaType.name(), actualCmd.mediaType());
    }

    @Test
    public void givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedVideoId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        when(uploadMediaUseCase.execute(any())).thenReturn(new UploadMediaOutput(expectedVideoId.getValue(), expectedType));

        final var aRequest = multipart("/videos/{id}/medias/{type}", expectedVideoId.getValue(), expectedType.name())
                .file(expectedVideo)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/videos/%s/medias/%s".formatted(expectedVideoId.getValue(), expectedType.name())))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.video_id", equalTo(expectedVideoId.getValue())))
                .andExpect(jsonPath("$.media_type", equalTo(expectedType.name())));

        final var captor = ArgumentCaptor.forClass(UploadMediaCommand.class);

        verify(this.uploadMediaUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();

        assertEquals(expectedVideoId.getValue(), actualCmd.videoId());
        assertEquals(expectedResource.content(), actualCmd.videoResource().resource().content());
        assertEquals(expectedResource.contentType(), actualCmd.videoResource().resource().contentType());
        assertEquals(expectedType, actualCmd.videoResource().type());
    }

    @Test
    public void givenAnInvalidMediaType_whenCallsUploadMedia_shouldReturnErrors() throws Exception {
        final var expectedVideoId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        final var aRequest = multipart("/videos/{id}/medias/INVALID", expectedVideoId.getValue())
                .file(expectedVideo)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo("Invalid INVALID for VideoMediaType")));

    }
}