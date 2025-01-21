package com.course.admin.catalogo.application.castmember.update;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.castmember.CastMemberType;
import com.course.admin.catalogo.domain.exceptions.NotFoundException;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;


    @Test
    public void givenAValidCastMember_whenCallingUpdateCastMemberWithValidCommand_shouldReturnItsIdentifier() {
        final var aMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand
                .with(expectedId.getValue(), expectedName,expectedType);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualPersistedMember = this.castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualPersistedMember.getName());
        assertEquals(expectedType, actualPersistedMember.getType());
        assertEquals(aMember.getCreatedAt(), actualPersistedMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(actualPersistedMember.getUpdatedAt()));

        verify(castMemberGateway, times(1)).findById(eq(expectedId));

        verify(castMemberGateway).update(any());

    }

    @Test
    public void givenAValidCastMember_whenCallingUpdateCastMemberWithInvalidName_shouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidCastMember_whenCallingUpdateCastMemberWithInvalidType_shouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName,expectedType);


        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInexistentID_whenCallingUpdateCastMember_shouldThrowsNotFoundException() {

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName,expectedType);

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }
}
