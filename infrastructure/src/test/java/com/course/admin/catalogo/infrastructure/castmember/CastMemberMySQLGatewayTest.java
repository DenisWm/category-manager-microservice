package com.course.admin.catalogo.infrastructure.castmember;

import com.course.admin.catalogo.MySQLGatewayTest;
import com.course.admin.catalogo.domain.Fixture;
import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.castmember.CastMemberType;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.course.admin.catalogo.domain.Fixture.CastMembers.type;
import static com.course.admin.catalogo.domain.Fixture.name;
import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testDependencies() {
        assertNotNull(castMemberGateway);
        assertNotNull(castMemberRepository);
    }

    @Test
    public void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        final var expectedName = name();
        final var expectedType = type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        assertEquals(0, castMemberRepository.count());

        final var actualMember = castMemberGateway.create(CastMember.with(aMember));

        assertEquals(1, castMemberRepository.count());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedId.getValue(), persistedMember.getId());
        assertEquals(expectedName, persistedMember.getName());
        assertEquals(expectedType, persistedMember.getType());
        assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldRefreshIt() {
        final var expectedName = name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();

        final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());
        assertEquals("vind", currentMember.getName());
        assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        final var actualMember = castMemberGateway.update(CastMember.with(aMember).update(expectedName, expectedType));

        assertEquals(1, castMemberRepository.count());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedId.getValue(), persistedMember.getId());
        assertEquals(expectedName, persistedMember.getName());
        assertEquals(expectedType, persistedMember.getType());
        assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallsDeleteById_shouldDeleteIt() {
        final var aMember = CastMember.newMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());

        castMemberGateway.deleteById(aMember.getId());

        assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeIgnored() {
        assertEquals(0, castMemberRepository.count());

        castMemberGateway.deleteById(CastMemberID.from("123"));

        assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenAValidCastMember_whenCallsFindById_shouldFindIt() {

        final var aMember = CastMember.newMember(name(), type());

        final var expectedId = aMember.getId();
        final var expectedName = aMember.getName();
        final var expectedType = aMember.getType();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());

        final var actualMember = castMemberGateway.findById(aMember.getId()).get();

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
        final var aMember = CastMember.newMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        assertEquals(1, castMemberRepository.count());

        final var actualMember = castMemberGateway.findById(CastMemberID.from("123"));

        assertFalse(actualMember.isPresent());
    }

    @Test
    public void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        assertEquals(0, castMemberRepository.count());
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "js,0,10,1,1,Json Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese"
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
    ) {
        mockMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Json Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt, asc,0,10,5,5,Kit Harington",
            "createdAt, desc,0,10,5,5,Martin Scorsese"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
    ) {
        mockMembers();

        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Json Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel"
    })
    public void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedNames
    ) {
        mockMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            assertEquals(expectedName, actualPage.items().get(index).getName());
            index++;
        }
    }

    @Test
    public void givenTwoCastMembersAndOnePersisted_whenCallsExistsById_shouldReturnPersistedId() {
        final var aCastMember = CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedItems = 1;

        final var expectedId = aCastMember.getId();

        assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));

        final var actualCategory = castMemberGateway
                .existsByIds(List.of(CastMemberID.from("123"), expectedId));

        assertEquals(expectedItems, actualCategory.size());
        assertEquals(expectedId.getValue() , actualCategory.get(0).getValue());
    }

    private void mockMembers() {
        castMemberRepository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Json Momoa", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
        ));
    }
}