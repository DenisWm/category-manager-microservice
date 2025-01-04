package com.course.admin.catalogo.application.castmember.retrieve.get;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.domain.castmember.CastMemberID;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
