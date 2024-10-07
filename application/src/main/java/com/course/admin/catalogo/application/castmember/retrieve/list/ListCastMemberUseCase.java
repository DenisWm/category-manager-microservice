package com.course.admin.catalogo.application.castmember.retrieve.list;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;

public sealed abstract class ListCastMemberUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMemberUseCase {
}
