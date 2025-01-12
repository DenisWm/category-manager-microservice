package com.course.admin.catalogo.infrastructure.services.impl;

import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.resource.Resource;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.video.VideoMediaType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class GCStorageServiceTest {

    private GCStorageService target;

    private Storage storage;

    private String bucket = "bucket";

    @BeforeEach
    public void setUp() {
        this.storage = mock(Storage.class);
        target = new GCStorageService(this.bucket, this.storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedName = IDUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        
        final var blob =  mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

        this.target.store(expectedName, expectedResource);

        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();

        assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        assertEquals(expectedName, actualBlob.getBlobId().getName());
        assertEquals(expectedName, actualBlob.getName());
        assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedName = IDUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob =  mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).get(anyString(), anyString());

        final var actualResource = this.target.get(expectedName).get();


        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldReturnEmpty() {
        final var expectedName = IDUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        doReturn(null).when(storage).get(anyString(), anyString());

        final var actualResource = this.target.get(expectedName);

        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_shouldRetrieveAll() {

        final var expectedPrefix = "media_";
        final var expectedNameVideo = expectedPrefix + IDUtils.uuid();
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedNameBanner = expectedPrefix + IDUtils.uuid();
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        final var blobVideo =  mockBlob(expectedNameVideo, expectedVideo);
        final var blobBanner =  mockBlob(expectedNameBanner, expectedBanner);

        final var page = Mockito.mock(Page.class);

        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();

        doReturn(page).when(storage).list(anyString(), any());

        final var actualResources = this.target.list(expectedPrefix);

        verify(storage, times(1)).list(eq(this.bucket), eq(Storage.BlobListOption.prefix(expectedPrefix)));

        assertTrue(expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources));
    }

    @Test
    public void givenValidNames_whenCallsDeleteAll_shouldDeleteAll() {
        final var expectedPrefix = "media_";
        final var expectedNameVideo = expectedPrefix + IDUtils.uuid();

        final var expectedNameBanner = expectedPrefix + IDUtils.uuid();

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        this.target.deleteAll(expectedResources);

        final var captor = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(captor.capture());

        final var actualResources = ((List<BlobId>) captor.getValue()).stream()
                .map(BlobId::getName)
                .toList();

        assertTrue(expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources));
    }

    private Blob mockBlob(final String name, final Resource resource) {
        final var blob = mock(Blob.class);
        when(blob.getBlobId()).thenReturn(BlobId.of(bucket, name));
        when(blob.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(blob.getContent()).thenReturn(resource.content());
        when(blob.getContentType()).thenReturn(resource.contentType());
        when(blob.getName()).thenReturn(resource.name());
        return blob;
    }

}