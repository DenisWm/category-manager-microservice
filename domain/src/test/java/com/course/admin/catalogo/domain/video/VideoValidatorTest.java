package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.UnitTest;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.DomainException;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VideoValidatorTest extends UnitTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        final String expectedTitle = null;
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
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

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

        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "";
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
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

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

        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = """
                    O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
                    O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
                    Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
                    É uma ótima prática para quem deseja dominar entrevistas de design.
                """;
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
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

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

        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "System Design Interviews Mock";
        final var expectedDescription = "";
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

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

        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "System Design Interviews Mock";
        final var expectedDescription = """
           The video "System Design Interviews Mock" is an in-depth simulation designed to help viewers understand and master the complexities of system design interviews, which are crucial for landing roles in top-tier tech companies. These interviews require candidates to demonstrate their ability to design scalable, reliable, and efficient systems under various constraints. In this mock interview, the candidate is tasked with designing a real-world system, such as a large-scale messaging service, a social media platform, or an online video streaming solution. The problem statement is carefully chosen to cover key aspects of system design, including scalability, performance, availability, and fault tolerance.
        
           The session starts with the interviewer presenting the high-level requirements of the system. For example, if the task involves designing a messaging platform, the requirements might include handling millions of concurrent users, ensuring low-latency message delivery, and supporting features like message persistence and search. The candidate begins by clarifying the requirements, asking questions to fully understand the scope and constraints of the problem. This step emphasizes the importance of clear communication and a structured approach to problem-solving, both of which are vital in real-world design scenarios.
    
           After the requirements are clear, the candidate moves on to designing the system. They typically start by outlining the system’s overall architecture, breaking it down into key components like clients, servers, load balancers, databases, and caching layers. For instance, in a messaging system, the candidate might discuss the role of application servers in handling user requests, the use of WebSocket connections for real-time communication, and the implementation of a distributed database for message storage. The use of visual aids, such as diagrams and flowcharts, helps to clearly convey the proposed architecture and its components.
    
           The candidate then delves deeper into the design, addressing specific challenges and trade-offs. They discuss how to handle high traffic volumes using techniques like horizontal scaling, database sharding, and caching. They explore consistency models, comparing strong consistency versus eventual consistency, and justify their choices based on the system’s requirements. For example, a messaging system might prioritize availability over strict consistency to ensure users can send and receive messages even during network partitions.
    
           Throughout the discussion, the interviewer engages actively, posing challenging questions to test the candidate’s depth of knowledge and adaptability. For instance, they might ask how the system would handle a sudden spike in traffic or how it would recover from a data center failure. The candidate is expected to propose solutions, such as implementing auto-scaling groups to handle increased load or setting up multi-region deployments to ensure high availability. These interactions provide valuable insights into the kinds of questions candidates can expect during real interviews and how to address them effectively.
    
           The video also covers non-functional requirements like monitoring, logging, and security. The candidate discusses strategies for tracking system performance, detecting anomalies, and securing user data. They might propose setting up a centralized logging system for troubleshooting or using encryption to protect sensitive information. These discussions highlight the importance of designing not just for functionality but also for maintainability and security.
    
           As the interview progresses, the candidate is asked to address failure scenarios and propose fault-tolerant designs. For example, they might discuss how to handle a database outage by implementing replication and failover mechanisms or how to ensure data durability using techniques like write-ahead logging. These topics demonstrate the candidate’s ability to think critically and design robust systems that can withstand real-world challenges.
    
           The session concludes with a review and feedback from the interviewer. The feedback covers the candidate’s strengths, such as their ability to break down the problem and communicate their ideas clearly, as well as areas for improvement, like exploring alternative solutions or providing more detailed justifications for certain design choices. This feedback is invaluable for viewers, offering practical advice on how to improve their own performance in system design interviews.
    
           In addition to the mock interview itself, the video includes tips and best practices for preparing for system design interviews. It emphasizes the importance of practicing with a variety of problems, studying existing system architectures, and staying updated on the latest trends and technologies in the field. By following these guidelines, viewers can build a solid foundation in system design and increase their chances of success in interviews.
    
           Overall, "System Design Interviews Mock" is a must-watch for software engineers, system architects, and aspiring tech leaders. It offers a realistic glimpse into the interview process, providing both theoretical insights and practical techniques for tackling complex design problems. Whether you’re preparing for an upcoming interview or simply looking to enhance your system design skills, this video is an excellent resource to help you achieve your goals.
                """;
        final var expectedLaunchedAt = Year.of(2017);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";

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

        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "System Design Interviews Mock";
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
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

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

        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "System Design Interviews Mock";
        final var expectedDescription = """
                    O vídeo "System Design Interviews Mock" simula uma entrevista técnica sobre design de sistemas, focando em escalabilidade, disponibilidade e performance. 
                    O candidato aborda problemas como projetar um sistema de mensagens em tempo real, explicando decisões arquiteturais. 
                    Diagramas e feedback detalhado ajudam a ilustrar as escolhas. 
                    É uma ótima prática para quem deseja dominar entrevistas de design.
                """;
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

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

        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }


}
