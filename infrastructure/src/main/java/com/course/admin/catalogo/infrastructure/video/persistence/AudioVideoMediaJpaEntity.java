package com.course.admin.catalogo.infrastructure.video.persistence;

import com.course.admin.catalogo.domain.video.AudioVideoMedia;
import com.course.admin.catalogo.domain.video.MediaStatus;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Table(name = "videos_video_media")
@Entity(name = "AudioVideoMedia")
public class AudioVideoMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "checksum", nullable = false)
    private String checksum;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;

    @Column(name = "media_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    public AudioVideoMediaJpaEntity() {
    }

    private AudioVideoMediaJpaEntity(
            final String id,
            final String checksum,
            final String name,
            final String filePath,
            final String encodedPath,
            final MediaStatus status
    ) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioVideoMediaJpaEntity from(final AudioVideoMedia media) {
        return new AudioVideoMediaJpaEntity(media.id(), media.checksum(), media.name(), media.rawLocation(), media.encodedLocation(), media.status());
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
            this.id,
            this.checksum,
            this.name,
            this.filePath,
            this.encodedPath,
            this.status
        );
    }

    public String getId() {
        return id;
    }

    public AudioVideoMediaJpaEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public AudioVideoMediaJpaEntity setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public String getName() {
        return name;
    }

    public AudioVideoMediaJpaEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public AudioVideoMediaJpaEntity setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public AudioVideoMediaJpaEntity setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
        return this;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public AudioVideoMediaJpaEntity setStatus(MediaStatus status) {
        this.status = status;
        return this;
    }
}
