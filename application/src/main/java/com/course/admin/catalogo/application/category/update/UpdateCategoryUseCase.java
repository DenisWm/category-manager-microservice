package com.course.admin.catalogo.application.category.update;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
