package com.course.admin.catalogo.infrastructure.video.persistence;

import com.course.admin.catalogo.domain.video.AudioVideoMedia;
import com.course.admin.catalogo.domain.video.ImageMedia;
import com.course.admin.catalogo.domain.video.MediaStatus;

import javax.persistence.*;

@Table(name = "videos_image_media")
@Entity(name = "ImageMedia")
public class ImageMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    public ImageMediaJpaEntity() {
    }

    private ImageMediaJpaEntity(
            final String id,
            final String name,
            final String filePath
    ) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    public static ImageMediaJpaEntity from(final ImageMedia media) {
        return new ImageMediaJpaEntity(media.checksum(), media.name(), media.location());
    }

    public ImageMedia toDomain() {
        return ImageMedia.with(
            this.id,
            this.name,
            this.filePath
        );
    }
}
