package com.course.admin.catalogo.domain.castmember;

import com.course.admin.catalogo.domain.UnitTest;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallNewCastMember_thenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualCastMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());
        Assertions.assertEquals(actualCastMember.getCreatedAt(), actualCastMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCastMember_thenShouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }
    @Test
    public void givenAnInvalidEmptyName_whenCallNewCastMember_thenShouldReceiveANotification() {
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
    @Test
    public void givenAnInvalidNameWithLengthMoreThan255_whenCallNewCastMember_thenShouldReceiveNotification() {
        final String expectedName = """
                Caros amigos, o comprometimento entre as equipes nos obriga à análise das condições financeiras e administrativas exigidas. 
                Por outro lado, a complexidade dos estudos efetuados prepara-nos para enfrentar situações atípicas decorrentes do orçamento setorial. 
                Gostaria de enfatizar que o entendimento das metas propostas obstaculiza a apreciação da importância das formas de ação. 
                Por conseguinte, a estrutura atual da organização talvez venha a ressaltar a relatividade das posturas dos órgãos dirigentes com relação às suas atribuições.
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
    @Test
    public void givenAnInvalidNullType_whenCallNewCastMember_thenShouldReceiveNotification() {
        final var expectedName = "Vin Viesel";
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateCastMember_thenShouldReceiveUpdated() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();

        actualMember.update(expectedName, expectedType);

        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }

    @Test
    public void givenAnInvalidNullName_whenCallUpdateCastMember_thenShouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallUpdateCastMember_thenShouldReceiveANotification() {
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAnInvalidNameWithLengthMoreThan255_whenCallUpdateCastMember_thenShouldReceiveANotification() {
        final String expectedName = """
                Caros amigos, o comprometimento entre as equipes nos obriga à análise das condições financeiras e administrativas exigidas. 
                Por outro lado, a complexidade dos estudos efetuados prepara-nos para enfrentar situações atípicas decorrentes do orçamento setorial. 
                Gostaria de enfatizar que o entendimento das metas propostas obstaculiza a apreciação da importância das formas de ação. 
                Por conseguinte, a estrutura atual da organização talvez venha a ressaltar a relatividade das posturas dos órgãos dirigentes com relação às suas atribuições.
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAnInvalidEmptyType_whenCallUpdateCastMember_thenShouldReceiveANotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}
