package com.course.admin.catalogo.domain.video;

import java.util.Arrays;
import java.util.Optional;

public enum VideoMediaType {
    VIDEO, TRAILER, BANNER, THUMBNAIL, THUMBNAIL_HALF;

    public static Optional<VideoMediaType> of(final String value) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(value)).
                findFirst();
    }
}
