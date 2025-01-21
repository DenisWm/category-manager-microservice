package com.course.admin.catalogo.infrastructure.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public final class HashingUtils {

    private static HashFunction CHECKSUM = Hashing.crc32c();

    private HashingUtils() {}

    public static String checksum(final byte[] content) {
        return CHECKSUM.hashBytes(content).toString();
    }
}
