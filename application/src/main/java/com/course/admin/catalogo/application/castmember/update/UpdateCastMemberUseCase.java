package com.course.admin.catalogo.application.castmember.update;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.course.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.course.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
