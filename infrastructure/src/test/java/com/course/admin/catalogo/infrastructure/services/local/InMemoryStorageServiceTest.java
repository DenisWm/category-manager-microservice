package com.course.admin.catalogo.infrastructure.services.local;

import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageServiceTest {

    private InMemoryStorageService target = new InMemoryStorageService();

    public void setUp() {
        this.target.reset();
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = IDUtils.uuid();

        target.store(expectedName, expectedResource);

        target.storage().get(expectedName);

        assertEquals(expectedResource, target.storage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = IDUtils.uuid();

        target.storage().put(expectedName, expectedResource);

        final var actualResource = target.get(expectedName).get();

        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldReturnEmpty() {
        final var actualResource = target.get("random name");

        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_shouldRetrieveAll() {

        final var expectedNames = List.of(
                "videos_" + IDUtils.uuid(),
                "videos_" + IDUtils.uuid(),
                "videos_" + IDUtils.uuid()
        );

        final var all = new ArrayList<>(expectedNames);

        all.add("image_" + IDUtils.uuid());
        all.add("image_" + IDUtils.uuid());

        all.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        assertEquals(5, target.storage().size());

        final var actualResource = target.list("videos");

        assertTrue(
                expectedNames.size() == actualResource.size() &&
                        expectedNames.containsAll(actualResource) &&
                        actualResource.containsAll(expectedNames)
        );
    }

    @Test
    public void givenValidNames_whenCallsDeleteAll_shouldDeleteAll() {

        final var videos = List.of(
                "videos_" + IDUtils.uuid(),
                "videos_" + IDUtils.uuid(),
                "videos_" + IDUtils.uuid()
        );
        final var expectedNames = Set.of(
                "image_" + IDUtils.uuid(),
                "image_" + IDUtils.uuid()
        );

        final var all = new ArrayList<>(videos);
        all.addAll(expectedNames);

        all.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        assertEquals(5, target.storage().size());

        target.deleteAll(videos);

        assertEquals(2, target.storage().size());

        final var actualKeys = target.storage().keySet();

        assertTrue(
                expectedNames.size() == actualKeys.size() &&
                        expectedNames.containsAll(actualKeys) &&
                        actualKeys.containsAll(expectedNames)
        );

        assertEquals(expectedNames, actualKeys);
    }
}