package com.course.admin.catalogo.application.video.media.update;

import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.video.MediaStatus;
import com.course.admin.catalogo.domain.video.Video;
import com.course.admin.catalogo.domain.video.VideoGateway;
import com.course.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateMediaStatusUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenACommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().setVideo(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        assertTrue(aVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        assertEquals(expectedMedia.id(), actualVideoMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        assertEquals(expectedStatus, actualVideoMedia.status());
        assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualVideoMedia.encodedLocation());
    }

    @Test
    public void givenACommandForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().setVideo(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        assertTrue(aVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        assertEquals(expectedMedia.id(), actualVideoMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        assertEquals(expectedStatus, actualVideoMedia.status());
        assertTrue(actualVideoMedia.encodedLocation().isBlank());
    }

    @Test
    public void givenACommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        assertTrue(aVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        assertEquals(expectedMedia.id(), actualVideoMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        assertEquals(expectedStatus, actualVideoMedia.status());
        assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualVideoMedia.encodedLocation());
    }

    @Test
    public void givenACommandForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        assertTrue(aVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        assertEquals(expectedMedia.id(), actualVideoMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        assertEquals(expectedStatus, actualVideoMedia.status());
        assertTrue(actualVideoMedia.encodedLocation().isBlank());
    }

    @Test
    public void givenACommandForTrailer_whenIsInvalid_shouldDoNothing() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().setVideo(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));

        final var aCommand = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                "randomId",
                expectedFolder,
                expectedFilename
        );

        this.useCase.execute(aCommand);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(videoGateway, times(0)).update(any());
    }
}
