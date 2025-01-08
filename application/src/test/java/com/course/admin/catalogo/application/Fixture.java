package com.course.admin.catalogo.application;

import com.course.admin.catalogo.domain.castmember.CastMember;
import com.course.admin.catalogo.domain.castmember.CastMemberType;
import com.course.admin.catalogo.domain.category.Category;
import com.course.admin.catalogo.domain.genre.Genre;
import com.course.admin.catalogo.domain.video.Rating;
import com.course.admin.catalogo.domain.video.Resource;
import com.course.admin.catalogo.domain.video.Video;
import com.github.javafaker.Faker;

import java.time.Year;
import java.util.Arrays;
import java.util.Set;

import static io.vavr.API.*;

public final class Fixture {
    
    private static final Faker FAKER = new Faker();
    
    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.random().nextDouble() * 10;
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Videos.description(),
                Year.of(year()),
                duration(),
                Videos.rating(),
                bool(),
                bool(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.denis().getId(), CastMembers.wesley().getId())
        );
    }

    public static final class Categories {

        private static final Category AULAS = Category.newCategory("Aulas", "Some description", true);

        public static Category aulas() {
            return Category.with(AULAS);
        }
    }

    public static final class Genres {

        private static final Genre TECH = Genre.newGenre("Technology",  true);

        public static Genre tech() {
            return Genre.with(TECH);
        }
    }

    public static final class CastMembers {

        private static final CastMember WESLEY = CastMember.newMember("Wesley FullCycle", CastMemberType.ACTOR);
        private static final CastMember DENIS = CastMember.newMember("Denis Mamoni", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember denis() {
            return CastMember.with(DENIS);
        }
    }

    public static final class Videos {

        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                Year.of(year()),
                duration(),
                rating(),
                bool(),
                bool(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.denis().getId(), CastMembers.wesley().getId())
        );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static String description() {
            return FAKER.lorem().paragraph();
        }

        public static Rating rating() {
            return FAKER.options().option(
                    Rating.values()
            );
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), () -> "video/mp4"),
                    Case($(), () -> "image/jpeg")
            );
            final byte[] content = FAKER.lorem().characters().getBytes();
            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }
    }
}
