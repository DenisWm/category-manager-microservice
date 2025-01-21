package com.course.admin.catalogo;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@DataJpaTest
@ComponentScan(
        basePackages = "com.course.admin.catalogo",
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
        }
)
@ExtendWith(MySQLCleanUpExtension.class)
@Tag("integrationTest")
public @interface MySQLGatewayTest {


}
