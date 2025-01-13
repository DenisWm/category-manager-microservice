package com.course.admin.catalogo.infrastructure.video;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.resource.Resource;
import com.course.admin.catalogo.domain.video.*;
import com.course.admin.catalogo.infrastructure.services.StorageService;
import com.course.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.course.admin.catalogo.domain.Fixture.Videos.mediaType;
import static com.course.admin.catalogo.domain.Fixture.Videos.resource;
import static org.junit.jupiter.api.Assertions.*;


@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private StorageService storageService;

    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @BeforeEach
    void setUp() {
        storageService().reset();
    }

    @Test
    public void testInjection() {
        assertNotNull(mediaResourceGateway);
        assertInstanceOf(DefaultMediaResourceGateway.class, mediaResourceGateway);

        assertNotNull(storageService);
        assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        final var actualMedia = this.mediaResourceGateway
                .storeAudioVideo(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        assertNotNull(actualMedia.id());
        assertEquals(expectedLocation, actualMedia.rawLocation());
        assertEquals(expectedResource.name(), actualMedia.name());
        assertEquals(expectedResource.checksum(), actualMedia.checksum());
        assertEquals(expectedStatus, actualMedia.status());
        assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

        final var actualStored = storageService().storage().get(expectedLocation);

        assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        final var actualMedia = this.mediaResourceGateway
                .storeImage(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        assertNotNull(actualMedia.id());
        assertEquals(expectedLocation, actualMedia.location());
        assertEquals(expectedResource.name(), actualMedia.name());
        assertEquals(expectedResource.checksum(), actualMedia.checksum());

        final var actualStored = storageService().storage().get(expectedLocation);

        assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsGetResource_shouldReturnIt() {
        final var videoId = VideoID.unique();
        final var expectedMediaType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedMediaType);

        storageService().store("videoId-%s/type-%s".formatted(videoId.getValue(), VideoMediaType.VIDEO.name()), expectedResource);
        storageService().store("videoId-%s/type-%s".formatted(videoId.getValue(), VideoMediaType.TRAILER.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoId.getValue(), VideoMediaType.BANNER.name()), resource(mediaType()));

        assertEquals(3, storageService().storage().size());

        final var actualResource = this.mediaResourceGateway.getResource(videoId, expectedMediaType).get();

        assertEquals(expectedResource.name(), actualResource.name());
        assertEquals(expectedResource.checksum(), actualResource.checksum());
        assertEquals(expectedResource.contentType(), actualResource.contentType());
    }

    @Test
    public void givenInvalidType_whenCallsGetResource_shouldReturnEmpty() {
        final var videoId = VideoID.unique();
        final var expectedMediaType = VideoMediaType.BANNER;

        storageService().store("videoId-%s/type-%s".formatted(videoId, VideoMediaType.VIDEO.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoId, VideoMediaType.TRAILER.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoId, VideoMediaType.BANNER.name()), resource(mediaType()));

        assertEquals(3, storageService().storage().size());

        final var actualResource = this.mediaResourceGateway.getResource(videoId, expectedMediaType);

        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenInvalidVideoId_whenCallsGetResource_shouldReturnEmpty() {
        final var videoId = VideoID.unique();
        final var expectedMediaType = VideoMediaType.VIDEO;

        storageService().store("videoId-%s/type-%s".formatted(videoId, VideoMediaType.VIDEO.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoId, VideoMediaType.TRAILER.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoId, VideoMediaType.BANNER.name()), resource(mediaType()));

        assertEquals(3, storageService().storage().size());

        final var actualResource = this.mediaResourceGateway.getResource(VideoID.from("123"), expectedMediaType);

        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(id -> storageService().store(id, Fixture.Videos.resource(mediaType())));

        expectedValues.forEach(id -> storageService().store(id, Fixture.Videos.resource(mediaType())));

        assertEquals(5, storageService().storage().size());

        this.mediaResourceGateway.clearResources(videoOne);

        assertEquals(2, storageService().storage().size());

        final var keySet = storageService().storage().keySet();

        assertTrue(expectedValues.size() == keySet.size() &&
                expectedValues.containsAll(keySet));
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }
}