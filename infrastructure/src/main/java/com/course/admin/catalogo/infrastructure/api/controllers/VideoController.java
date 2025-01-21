package com.course.admin.catalogo.infrastructure.api.controllers;

import com.course.admin.catalogo.application.video.create.CreateVideoCommand;
import com.course.admin.catalogo.application.video.create.CreateVideoOutput;
import com.course.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.course.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.course.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.course.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.course.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.course.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.course.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.course.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.course.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.course.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.course.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.exceptions.DomainException;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.genre.GenreID;
import com.course.admin.catalogo.domain.pagination.Pagination;
import com.course.admin.catalogo.domain.resource.Resource;
import com.course.admin.catalogo.domain.validation.Error;
import com.course.admin.catalogo.domain.video.VideoMediaType;
import com.course.admin.catalogo.domain.video.VideoResource;
import com.course.admin.catalogo.domain.video.VideoSearchQuery;
import com.course.admin.catalogo.infrastructure.api.VideoAPI;
import com.course.admin.catalogo.infrastructure.utils.HashingUtils;
import com.course.admin.catalogo.infrastructure.video.models.*;
import com.course.admin.catalogo.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static com.course.admin.catalogo.domain.utils.CollectionUtils.mapTo;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;

    private final GetVideoByIdUseCase getVideoByIdUseCase;

    private final UpdateVideoUseCase updateVideoUseCase;

    private final DeleteVideoUseCase deleteVideoUseCase;

    private final ListVideoUseCase listVideoUseCase;

    private final GetMediaUseCase getMediaUseCase;

    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(final CreateVideoUseCase createVideoUseCase,
                           final GetVideoByIdUseCase getVideoByIdUseCase,
                           final UpdateVideoUseCase updateVideoUseCase,
                           final DeleteVideoUseCase deleteVideoUseCase,
                           final ListVideoUseCase listVideoUseCase,
                           final GetMediaUseCase getMediaUseCase,
                           final UploadMediaUseCase uploadMediaUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(
            final String title,
            final String description,
            final Integer yearLaunched,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbnailFile,
            final MultipartFile thumbnailHalfFile
    ) {
        final var aCommand = CreateVideoCommand.with(
                title,
                description,
                yearLaunched,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbnailFile),
                resourceOf(thumbnailHalfFile)
        );
        final var output = this.createVideoUseCase.execute(aCommand);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest aRequest) {
        final var aCommand = CreateVideoCommand.with(
                aRequest.title(),
                aRequest.description(),
                aRequest.launchedAt(),
                aRequest.duration(),
                aRequest.opened(),
                aRequest.published(),
                aRequest.rating(),
                aRequest.categories(),
                aRequest.genres(),
                aRequest.members(),
                null,
                null,
                null,
                null,
                null
        );
        final var output = this.createVideoUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponse getById(final String id) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateVideo(final String id, final UpdateVideoRequest aRequest) {
        final var aCmd = UpdateVideoCommand.with(
                id,
                aRequest.title(),
                aRequest.description(),
                aRequest.launchedAt(),
                aRequest.duration(),
                aRequest.opened(),
                aRequest.published(),
                aRequest.rating(),
                aRequest.categories(),
                aRequest.genres(),
                aRequest.members()
        );

        final var output = this.updateVideoUseCase.execute(aCmd);

        return ResponseEntity.ok(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteVideoUseCase.execute(id);
    }

    @Override
    public Pagination<VideoListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String dir,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> castMembers
    ) {
        final var aQuery = new VideoSearchQuery(
                page,
                perPage,
                search,
                sort,
                dir,
                mapTo(castMembers, CastMemberID::from),
                mapTo(categories, CategoryID::from),
                mapTo(genres, GenreID::from)
        );

        final var output = listVideoUseCase.execute(aQuery);

        return output.map(VideoApiPresenter::present);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String id, final String type) {
        final var aMedia = this.getMediaUseCase.execute(GetMediaCommand.with(id, type));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(aMedia.name()))
                .body(aMedia.content());
    }

    @Override
    public ResponseEntity<UploadMediaResponse> uploadMediaByType(
            final String id,
            final String type,
            final MultipartFile mediaFile
    ) {

        final var videoMediaType = VideoMediaType.of(type).orElseThrow(illegalType(type));
        final var resource = resourceOf(mediaFile);
        final var videoResource = VideoResource.with(resource,
                videoMediaType);

        final var aCmd = UploadMediaCommand.with(id, videoResource);

        final var output = this.uploadMediaUseCase.execute(aCmd);

        return ResponseEntity
                .created(URI.create("/videos/%s/medias/%s".formatted(id, type)))
                .body(VideoApiPresenter.present(output));
    }

    private static Supplier<DomainException> illegalType(final String type) {
        return () -> NotificationException.with(new Error("Invalid %s for VideoMediaType".formatted(type)));
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }
        try {
            return Resource.with(
                    HashingUtils.checksum(part.getBytes()),
                    part.getBytes(),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
