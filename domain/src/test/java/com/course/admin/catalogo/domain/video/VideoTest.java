package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import javax.management.Descriptor;
import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
            O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
            O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
            Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
            É uma ótima prática para quem deseja dominar entrevistas de design.
        """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertTrue(expectedCategories.containsAll(actualVideo.getCategories()));
        assertTrue(expectedGenres.containsAll(actualVideo.getGenres()));
        assertTrue(expectedMembers.containsAll(actualVideo.getCastMembers()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbNail().isEmpty());
        assertTrue(actualVideo.getThumbNailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }
}
