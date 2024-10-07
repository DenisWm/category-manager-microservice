package com.course.admin.catalogo.application.castmember.retrieve.list;

import com.course.admin.catalogo.application.Fixture;
import com.course.admin.catalogo.application.UseCaseTest;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListCastMemberUseCase extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }
    @Test
    public void givenAValidQuery_whenListCastMembers_shouldReturnAll() {
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

       final var expectedItems = members.stream().map(CastMemberListOutput::from).toList();

       final var expectedPagination = new Pagination<>(
               expectedPage,
               expectedPerPage,
               expectedTotal,
               members
       );

       when(castMemberGateway.findAll(any())).thenReturn(expectedPagination);

       final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

       final var actualOutput = useCase.execute(aQuery);

       assertEquals(expectedPage, actualOutput.currentPage());
       assertEquals(expectedPerPage, actualOutput.perPage());
       assertEquals(expectedTotal, actualOutput.total());
       assertEquals(expectedItems, actualOutput.items());

       verify(castMemberGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenListCastMembersAndIsEmpty_shouldReturn() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedMembers = List.<CastMember>of();
        final var expectedItems = List.<CastMemberListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedMembers
        );

        when(castMemberGateway.findAll(any())).thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(castMemberGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenListCastMembersAndGatewayThrowsRandomException_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";


        when(castMemberGateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findAll(eq(aQuery));
    }
}
