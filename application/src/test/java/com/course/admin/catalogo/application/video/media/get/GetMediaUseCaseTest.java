package com.course.admin.catalogo.application.video.media.get;

import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.video.MediaResourceGateway;
import com.course.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    public void givenVideoIdAndType_whenIsValidCmd_shouldReturnResource() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType)).thenReturn(Optional.of(expectedResource));

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        final var actualResult = this.useCase.execute(aCmd);

        assertEquals(expectedResource.name(), actualResult.name());
        assertEquals(expectedResource.contentType(), actualResult.contentType());
        assertEquals(expectedResource.content(), actualResult.content());
    }

    @Test
    public void givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedErrorMessage = "Resource %s not found for video %s"
                .formatted(expectedType.name(), expectedId.getValue());


        when(mediaResourceGateway.getResource(expectedId, expectedType)).thenReturn(Optional.empty());

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(aCmd));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = "Nonexistent media type";
        final var expectedErrorMessage = "Media type %s does not exists"
                .formatted(expectedType);

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType);

        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(aCmd));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
