package com.course.admin.catalogo.infrastructure.amqp;

import com.course.admin.catalogo.AmqpTest;
import com.course.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.course.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.course.admin.catalogo.domain.utils.IDUtils;
import com.course.admin.catalogo.domain.video.MediaStatus;
import com.course.admin.catalogo.infrastructure.configuration.Json;
import com.course.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.course.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.course.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted;
import com.course.admin.catalogo.infrastructure.video.models.VideoEncoderError;
import com.course.admin.catalogo.infrastructure.video.models.VideoMessage;
import com.course.admin.catalogo.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@AmqpTest
public class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    public void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
        final var expectedError = new VideoEncoderError(
                new VideoMessage("123", "abc"),
                "Video not found"
        );

        final var expectedMessage = Json.writeValueAsString(expectedError);

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void GivenCompletedResult_whenCallsListener_shouldProcess() throws InterruptedException {
        final var expectedId = IDUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IDUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

        final var expectedResult = new VideoEncoderCompleted(
                expectedId,
                expectedOutputBucket,
                expectedMetadata
        );

        final var expectedMessage = Json.writeValueAsString(expectedResult);

        doNothing().when(updateMediaStatusUseCase).execute(any());

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
        verify(updateMediaStatusUseCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();

        assertEquals(expectedId, actualCommand.videoId());
        assertEquals(expectedStatus, actualCommand.status());
        assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
        assertEquals(expectedResourceId, actualCommand.resourceId());
        assertEquals(expectedFilePath, actualCommand.filename());
    }
}
