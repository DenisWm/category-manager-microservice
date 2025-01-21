package com.course.admin.catalogo.infrastructure.configuration;

import com.course.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.course.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.course.admin.catalogo.infrastructure.services.EventService;
import com.course.admin.catalogo.infrastructure.services.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    public EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations ops
            ) {
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
