package com.course.admin.catalogo.infrastructure.video.persistence;

import com.course.admin.catalogo.domain.video.AudioVideoMedia;
import com.course.admin.catalogo.domain.video.MediaStatus;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "videos_video_media")
@Entity(name = "AudioVideoMedia")
public class AudioVideoMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    public AudioVideoMediaJpaEntity() {
    }

    private AudioVideoMediaJpaEntity(
            final String id,
            final String name,
            final String filePath,
            final String encodedPath,
            final MediaStatus status
    ) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioVideoMediaJpaEntity from(final AudioVideoMedia media) {
        return new AudioVideoMediaJpaEntity(media.checksum(), media.name(), media.rawLocation(), media.encodedLocation(), media.status());
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
            this.id,
            this.name,
            this.filePath,
            this.encodedPath,
            this.status
        );
    }
}
