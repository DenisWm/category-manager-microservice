package com.course.admin.catalogo.application.castmember.create;

import com.course.admin.catalogo.IntegrationTest;

import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberType;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;



    @Test
    public void givenAValidCommand_whenCallingCreateCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualMember = this.castMemberRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());
        assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());

        verify(castMemberGateway).create(any());
    }

    @Test
    public void givenAnInvalidName_whenCallingCreateCastMember_shouldThrowsNotificationException() {
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidType_whenCallingCreateCastMember_shouldThrowsNotificationException() {
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(castMemberGateway, times(0)).create(any());
    }
}
