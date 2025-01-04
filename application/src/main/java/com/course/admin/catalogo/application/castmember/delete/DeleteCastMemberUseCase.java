package com.course.admin.catalogo.application.castmember.delete;

import com.course.admin.catalogo.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase
{
}
