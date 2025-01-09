package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CastMemberID> castMember,
        Set<CategoryID> categories,
        Set<CastMemberID> genres
) {
}
