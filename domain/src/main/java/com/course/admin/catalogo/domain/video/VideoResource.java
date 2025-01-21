package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.ValueObject;
import com.course.admin.catalogo.domain.resource.Resource;

import java.util.Objects;

public class VideoResource extends ValueObject {

    private final Resource resource;
    private final VideoMediaType type;

    private VideoResource(final Resource resource, final VideoMediaType type) {
        this.resource = Objects.requireNonNull(resource);
        this.type = Objects.requireNonNull(type);
    }

    public static VideoResource with(final Resource resource, final VideoMediaType type) {
        return new VideoResource(resource, type);
    }

    public Resource resource() {
        return resource;
    }

    public VideoMediaType type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final VideoResource that = (VideoResource) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}
