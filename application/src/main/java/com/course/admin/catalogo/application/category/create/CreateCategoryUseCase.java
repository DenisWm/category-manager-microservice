package com.course.admin.catalogo.application.category.create;

import com.course.admin.catalogo.application.UseCase;
import com.course.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
