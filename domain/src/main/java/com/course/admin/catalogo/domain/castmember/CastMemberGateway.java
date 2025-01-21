package com.course.admin.catalogo.domain.castmember;

import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(CastMember aMember);
    void deleteById(CastMemberID anId);
    Optional<CastMember> findById(CastMemberID anId);
    CastMember update(CastMember aMember);
    Pagination<CastMember> findAll(SearchQuery aQuery);
    List<CastMemberID> existsByIds(Iterable<CastMemberID> ids);
}
