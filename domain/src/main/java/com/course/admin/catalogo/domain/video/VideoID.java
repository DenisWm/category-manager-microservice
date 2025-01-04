package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

    protected final String value;

    private VideoID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }
    public static VideoID unique() {
        return VideoID.from(UUID.randomUUID());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId);
    }
    public static VideoID from (final UUID anId) {
        return new VideoID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final VideoID videoID = (VideoID) o;

        return value.equals(videoID.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
