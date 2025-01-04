package com.course.admin.catalogo.infrastructure.configuration.usecases;

import com.course.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.course.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.course.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.course.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfiguration {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfiguration(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }
    @Bean
    public ListCastMemberUseCase listCastMemberUseCase() {
        return new DefaultListCastMemberUseCase(castMemberGateway);
    }
}
