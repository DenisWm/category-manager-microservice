package com.course.admin.catalogo.application.utils;

import com.course.admin.catalogo.domain.Identifier;

import java.util.List;

public final class IDUtils {

    private IDUtils(){
    }
    public static <T extends Identifier> List<String> asString(final List<T> ids) {
        return ids.stream().map(Identifier::getValue).toList();
    }
}
