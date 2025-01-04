package com.course.admin.catalogo.application.castmember.delete;

import com.course.admin.catalogo.application.Fixture;
import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidIdentifier_whenCallsDeleteMember_shouldDeleteIt() throws Exception {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = castMember.getId();

        doNothing().when(castMemberGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq((expectedId)));
    }

    @Test
    public void givenAnInvalidIdentifier_whenCallsDeleteMember_shouldBeOk() throws Exception {
        final var expectedId = CastMemberID.from("123");

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq((expectedId)));
    }

    @Test
    public void givenValidIdentifier_whenCallsDeleteMemberAndGatewayThrowsException_shouldReceiveException() throws Exception {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedId = castMember.getId();
        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(castMemberGateway).deleteById(eq((expectedId)));
    }

}
