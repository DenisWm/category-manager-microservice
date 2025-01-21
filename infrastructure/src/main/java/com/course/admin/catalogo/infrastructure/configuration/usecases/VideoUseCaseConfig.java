package com.course.admin.catalogo.infrastructure.configuration.usecases;


import com.course.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.course.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import com.course.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase;
import com.course.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.course.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase;
import com.course.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.course.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.course.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.course.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase;
import com.course.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.course.admin.catalogo.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.course.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.course.admin.catalogo.application.video.retrieve.list.DefaultListVideoUseCase;
import com.course.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.course.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase;
import com.course.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.course.admin.catalogo.domain.castmember.CastMemberGateway;
import com.course.admin.catalogo.domain.category.CategoryGateway;
import com.course.admin.catalogo.domain.genre.GenreGateway;
import com.course.admin.catalogo.domain.video.MediaResourceGateway;
import com.course.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final VideoGateway videoGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final CategoryGateway categoryGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public VideoUseCaseConfig(
            final VideoGateway videoGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final CategoryGateway categoryGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(
                videoGateway,
                categoryGateway,
                genreGateway,
                castMemberGateway,
                mediaResourceGateway
        );
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(
                videoGateway,
                categoryGateway,
                genreGateway,
                castMemberGateway,
                mediaResourceGateway
        );
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public ListVideoUseCase listVideoUseCase() {
        return new DefaultListVideoUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
