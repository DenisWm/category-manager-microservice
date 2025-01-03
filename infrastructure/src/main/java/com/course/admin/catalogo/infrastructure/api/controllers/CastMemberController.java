package com.course.admin.catalogo.infrastructure.api.controllers;

import com.course.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.course.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.course.admin.catalogo.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.course.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.pagination.SearchQuery;
import com.course.admin.catalogo.infrastructure.api.CastMemberAPI;
import com.course.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse;
import com.course.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import com.course.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import com.course.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.course.admin.catalogo.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMemberUseCase listCastMemberUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase,
            final ListCastMemberUseCase listCastMemberUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMemberUseCase = Objects.requireNonNull(listCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateCastMemberRequest input) {
        final var aCommand = UpdateCastMemberCommand.with(id, input.name(), input.type());

        final var output = this.updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void delete(final String id) {
        deleteCastMemberUseCase.execute(id);
    }

    @Override
    public Pagination<CastMemberListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listCastMemberUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberPresenter::present);
    }


}
