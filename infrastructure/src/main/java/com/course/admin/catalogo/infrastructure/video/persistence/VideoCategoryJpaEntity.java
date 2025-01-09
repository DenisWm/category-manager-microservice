package com.course.admin.catalogo.infrastructure.video.persistence;

import com.course.admin.catalogo.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Table(name = "videos_categories")
@Entity(name = "VideoCategory")
public class VideoCategoryJpaEntity {

    @EmbeddedId
    private VideoCategoryID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("video_id")
    private VideoJpaEntity video;

    public VideoCategoryJpaEntity() {
    }

    private VideoCategoryJpaEntity(VideoCategoryID id, VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCategoryJpaEntity from(final VideoJpaEntity video, final CategoryID category) {
        return new VideoCategoryJpaEntity(VideoCategoryID.from(video.getId(), category.getValue()), video);
    }

    public VideoCategoryID getId() {
        return id;
    }

    public VideoCategoryJpaEntity setId(VideoCategoryID id) {
        this.id = id;
        return this;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoCategoryJpaEntity setVideo(VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
