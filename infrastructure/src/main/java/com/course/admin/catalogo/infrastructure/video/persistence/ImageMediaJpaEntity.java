package com.course.admin.catalogo.infrastructure.video.persistence;

import com.course.admin.catalogo.domain.video.AudioVideoMedia;
import com.course.admin.catalogo.domain.video.ImageMedia;
import com.course.admin.catalogo.domain.video.MediaStatus;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "videos_image_media")
@Entity(name = "ImageMedia")
public class ImageMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "checksum", nullable = false)
    private String checksum;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    public ImageMediaJpaEntity() {
    }

    private ImageMediaJpaEntity(
            final String id,
            final String checksum,
            final String name,
            final String filePath
    ) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
    }

    public static ImageMediaJpaEntity from(final ImageMedia media) {
        return new ImageMediaJpaEntity(media.id(), media.checksum(), media.name(), media.location());
    }

    public ImageMedia toDomain() {
        return ImageMedia.with(
            this.id,
            this.checksum,
            this.name,
            this.filePath
        );
    }

    public String getId() {
        return id;
    }

    public ImageMediaJpaEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public ImageMediaJpaEntity setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public String getName() {
        return name;
    }

    public ImageMediaJpaEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public ImageMediaJpaEntity setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
}
