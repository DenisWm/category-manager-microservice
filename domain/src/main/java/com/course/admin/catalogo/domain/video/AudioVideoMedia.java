package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.ValueObject;
import com.course.admin.catalogo.domain.utils.IDUtils;

import java.util.Objects;

public class AudioVideoMedia extends ValueObject {

    private final String id;
    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    private AudioVideoMedia(
            final String id,
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status
    ) {
        this.id = Objects.requireNonNull(id);
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status
    ) {
        return new AudioVideoMedia(IDUtils.uuid(), checksum, name, rawLocation, encodedLocation, status);
    }

    public static AudioVideoMedia with(
            final String id,
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status
    ) {
        return new AudioVideoMedia(id, checksum, name, rawLocation, encodedLocation, status);
    }

    public static AudioVideoMedia with(
            final String checksum,
            final String name,
            final String rawLocation
    ) {
        return new AudioVideoMedia(IDUtils.uuid(), checksum, name, rawLocation, "", MediaStatus.PENDING);
    }

    public String id() {
        return id;
    }

    public String checksum() {
        return checksum;
    }

    public String name() {
        return name;
    }

    public String rawLocation() {
        return rawLocation;
    }

    public String encodedLocation() {
        return encodedLocation;
    }

    public MediaStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final AudioVideoMedia that = (AudioVideoMedia) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(rawLocation, that.rawLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, rawLocation);
    }
}
