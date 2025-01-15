package com.course.admin.catalogo.infrastructure.services.impl;

import com.course.admin.catalogo.AmqpTest;
import com.course.admin.catalogo.domain.video.VideoMediaCreated;
import com.course.admin.catalogo.infrastructure.configuration.Json;
import com.course.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.course.admin.catalogo.infrastructure.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.wildfly.common.Assert.assertNotNull;

@AmqpTest
public class RabbitEventServiceTest {

    private static final String LISTENER = "video.created";
    @Autowired
    @VideoCreatedQueue
    private EventService publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void shouldSendMessage() throws InterruptedException {
        final var notification = new VideoMediaCreated("resource", "filepath");

        final var expectedMessage =Json.writeValueAsString(notification);

        this.publisher.send(notification);

        final var invocationData = harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);
    }

    @Component
    static class VideoCreatedNewsListener {

        @RabbitListener(id = LISTENER, queues = "${amqp.queues.video-created.routing-key}")
        void onVideoCreated(@Payload String message) {

        }
    }
}
