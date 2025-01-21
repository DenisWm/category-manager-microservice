package com.course.admin.catalogo.domain.video;

import com.course.admin.catalogo.domain.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageMediaTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnNewInstance() {
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedLocation = "/images/ac";

        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        assertNotNull(actualImage);
        assertEquals(expectedChecksum, actualImage.checksum());
        assertEquals(expectedName, actualImage.name());
        assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedLocation = "/images/ac";

        final var img1 = ImageMedia.with(expectedChecksum, "Random", expectedLocation);
        final var img2 = ImageMedia.with(expectedChecksum, "Simple", expectedLocation);

        final var actualEquals = img1.equals(img2);

        assertTrue(actualEquals);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        assertThrows(NullPointerException.class,
                () -> ImageMedia.with(null, "Random", "/images/ac"));

        assertThrows(NullPointerException.class,
                () -> ImageMedia.with("abc", null, "/images/ac"));

        assertThrows(NullPointerException.class,
                () -> ImageMedia.with("abc", "Random", null));
    }
}
