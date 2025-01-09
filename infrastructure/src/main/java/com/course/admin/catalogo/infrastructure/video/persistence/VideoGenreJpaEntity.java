package com.course.admin.catalogo.infrastructure.video.persistence;

import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.genre.GenreID;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Table(name = "videos_genres")
@Entity(name = "VideoGenre")
public class VideoGenreJpaEntity {

    @EmbeddedId
    private VideoGenreID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("video_id")
    private VideoJpaEntity video;

    public VideoGenreJpaEntity() {
    }

    private VideoGenreJpaEntity(VideoGenreID id, VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoGenreJpaEntity from(final VideoJpaEntity video, final GenreID genre) {
        return new VideoGenreJpaEntity(VideoGenreID.from(video.getId(), genre.getValue()), video);
    }

    public VideoGenreID getId() {
        return id;
    }

    public VideoGenreJpaEntity setId(VideoGenreID id) {
        this.id = id;
        return this;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoGenreJpaEntity setVideo(VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
