package com.course.admin.catalogo.application.video.media.upload;

import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUploadMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway, videoGateway);
    }

    @Test
    public void givenCommandToUpload_whenIsValid_shouldUpdateVideoMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.systemDesign();

        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = audioVideo(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenReturn(expectedMedia);
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(expectedType, actualOutput.mediaType());
        assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(aVideo.getId(), actualVideo.getId())
                        && Objects.equals(aVideo.getTitle(), actualVideo.getTitle())
                        && Objects.equals(aVideo.getDescription(), actualVideo.getDescription())
                        && Objects.equals(aVideo.getRating(), actualVideo.getRating())
                        && Objects.equals(aVideo.getLaunchedAt(), actualVideo.getLaunchedAt())
                        && Objects.equals(aVideo.getDuration(), actualVideo.getDuration())
                        && Objects.equals(aVideo.isOpened(), actualVideo.isOpened())
                        && Objects.equals(aVideo.isPublished(), actualVideo.isPublished())
                        && Objects.equals(expectedMedia, actualVideo.getVideo().get())
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbNail().isEmpty()
                        && actualVideo.getThumbNailHalf().isEmpty()
        ));

    }

    @Test
    public void givenCommandToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.systemDesign();

        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = audioVideo(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenReturn(expectedMedia);
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(expectedType, actualOutput.mediaType());
        assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(aVideo.getId(), actualVideo.getId())
                        && Objects.equals(aVideo.getTitle(), actualVideo.getTitle())
                        && Objects.equals(aVideo.getDescription(), actualVideo.getDescription())
                        && Objects.equals(aVideo.getRating(), actualVideo.getRating())
                        && Objects.equals(aVideo.getLaunchedAt(), actualVideo.getLaunchedAt())
                        && Objects.equals(aVideo.getDuration(), actualVideo.getDuration())
                        && Objects.equals(aVideo.isOpened(), actualVideo.isOpened())
                        && Objects.equals(aVideo.isPublished(), actualVideo.isPublished())
                        && Objects.equals(expectedMedia, actualVideo.getTrailer().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbNail().isEmpty()
                        && actualVideo.getThumbNailHalf().isEmpty()
        ));

    }

    @Test
    public void givenCommandToUpload_whenIsValid_shouldUpdateBannerMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.systemDesign();

        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = image(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(expectedType, actualOutput.mediaType());
        assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(aVideo.getId(), actualVideo.getId())
                        && Objects.equals(aVideo.getTitle(), actualVideo.getTitle())
                        && Objects.equals(aVideo.getDescription(), actualVideo.getDescription())
                        && Objects.equals(aVideo.getRating(), actualVideo.getRating())
                        && Objects.equals(aVideo.getLaunchedAt(), actualVideo.getLaunchedAt())
                        && Objects.equals(aVideo.getDuration(), actualVideo.getDuration())
                        && Objects.equals(aVideo.isOpened(), actualVideo.isOpened())
                        && Objects.equals(aVideo.isPublished(), actualVideo.isPublished())
                        && Objects.equals(expectedMedia, actualVideo.getBanner().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getThumbNail().isEmpty()
                        && actualVideo.getThumbNailHalf().isEmpty()
        ));

    }

    @Test
    public void givenCommandToUpload_whenIsValid_shouldUpdateThumbnailMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.systemDesign();

        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = image(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(expectedType, actualOutput.mediaType());
        assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(aVideo.getId(), actualVideo.getId())
                        && Objects.equals(aVideo.getTitle(), actualVideo.getTitle())
                        && Objects.equals(aVideo.getDescription(), actualVideo.getDescription())
                        && Objects.equals(aVideo.getRating(), actualVideo.getRating())
                        && Objects.equals(aVideo.getLaunchedAt(), actualVideo.getLaunchedAt())
                        && Objects.equals(aVideo.getDuration(), actualVideo.getDuration())
                        && Objects.equals(aVideo.isOpened(), actualVideo.isOpened())
                        && Objects.equals(aVideo.isPublished(), actualVideo.isPublished())
                        && Objects.equals(expectedMedia, actualVideo.getThumbNail().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbNailHalf().isEmpty()
        ));

    }

    @Test
    public void givenCommandToUpload_whenIsValid_shouldUpdateThumbnailHalfMediaAndPersistIt() {
        final var aVideo = Fixture.Videos.systemDesign();

        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = image(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(aVideo)));
        when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(expectedType, actualOutput.mediaType());
        assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(aVideo.getId(), actualVideo.getId())
                        && Objects.equals(aVideo.getTitle(), actualVideo.getTitle())
                        && Objects.equals(aVideo.getDescription(), actualVideo.getDescription())
                        && Objects.equals(aVideo.getRating(), actualVideo.getRating())
                        && Objects.equals(aVideo.getLaunchedAt(), actualVideo.getLaunchedAt())
                        && Objects.equals(aVideo.getDuration(), actualVideo.getDuration())
                        && Objects.equals(aVideo.isOpened(), actualVideo.isOpened())
                        && Objects.equals(aVideo.isPublished(), actualVideo.isPublished())
                        && Objects.equals(expectedMedia, actualVideo.getThumbNailHalf().get())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbNail().isEmpty()
        ));

    }

    @Test
    public void givenCommandToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        final var aVideo = Fixture.Videos.systemDesign();

        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);

        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(videoGateway.findById(any())).thenReturn(Optional.empty());


        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorMessage, actualException.getMessage());

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
