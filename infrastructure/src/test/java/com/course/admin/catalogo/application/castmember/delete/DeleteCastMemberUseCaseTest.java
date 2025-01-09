package com.course.admin.catalogo.application.castmember.delete;

import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCastMemberUseCaseTest  {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenValidIdentifier_whenCallsDeleteMember_shouldDeleteIt() throws Exception {
        final var castMemberOne = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var castMemberTwo = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = castMemberOne.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMemberOne));
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMemberTwo));

        assertEquals(2, this.castMemberRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq((expectedId)));

        assertEquals(1, this.castMemberRepository.count());
        assertFalse(this.castMemberRepository.existsById(expectedId.getValue()));
        assertTrue(this.castMemberRepository.existsById(castMemberTwo.getId().getValue()));
    }

    @Test
    public void givenAnInvalidIdentifier_whenCallsDeleteMember_shouldBeOk() throws Exception {
        this.castMemberRepository.saveAndFlush(
                CastMemberJpaEntity.from(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())));

        final var expectedId = CastMemberID.from("123");

        assertEquals(1, this.castMemberRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq((expectedId)));

        assertEquals(1, this.castMemberRepository.count());
    }

    @Test
    public void givenValidIdentifier_whenCallsDeleteMemberAndGatewayThrowsException_shouldReceiveException() throws Exception {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMember));

        final var expectedId = castMember.getId();

        assertEquals(1, this.castMemberRepository.count());

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq((expectedId)));

        assertEquals(1, this.castMemberRepository.count());

    }

}
