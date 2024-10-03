package com.course.admin.catalogo.domain.castmember;

import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(CastMember aCategory);
    void deleteById(CastMemberID anId);
    Optional<CastMember> findById(CastMemberID anId);
    CastMember update(CastMember aCategory);
    Pagination<CastMember> findAll(SearchQuery aQuery);
}
