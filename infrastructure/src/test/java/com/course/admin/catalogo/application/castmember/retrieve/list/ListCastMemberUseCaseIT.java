package com.course.admin.catalogo.application.castmember.retrieve.list;

import com.course.admin.catalogo.Fixture;
import com.course.admin.catalogo.IntegrationTest;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class ListCastMemberUseCaseIT {

    @Autowired
    private ListCastMemberUseCase useCase;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidQuery_whenListCastMembers_shouldReturnAll() {
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
        );

        this.castMemberRepository.saveAllAndFlush(members.stream().map(CastMemberJpaEntity::from).toList());

        assertEquals(2, castMemberRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

       final var expectedItems = members.stream().map(CastMemberListOutput::from).toList();

       final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

       final var actualOutput = useCase.execute(aQuery);

       assertEquals(expectedPage, actualOutput.currentPage());
       assertEquals(expectedPerPage, actualOutput.perPage());
       assertEquals(expectedTotal, actualOutput.total());
       assertTrue(
               expectedItems.size() == actualOutput.items().size()
               && expectedItems.containsAll(actualOutput.items())
       );

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

        doThrow(new IllegalStateException(expectedErrorMessage)).when(castMemberGateway).findAll(any());

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findAll(eq(aQuery));
    }
}
