/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.zxing.spring.boot.client;

import com.google.zxing.LuminanceSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link BufferedImageLuminanceSource}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("BufferedImageLuminanceSource Tests")
class BufferedImageLuminanceSourceTest {

    private BufferedImage rgbImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.fillRect(0, 0, width, height);
        g.dispose();
        return image;
    }

    private BufferedImage grayImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    }

    @Test
    @DisplayName("Constructor wraps a TYPE_BYTE_GRAY image directly")
    void testGrayImageConstructor() {
        BufferedImage image = grayImage(10, 8);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        assertThat(source.getWidth()).isEqualTo(10);
        assertThat(source.getHeight()).isEqualTo(8);
        assertThat(source.isCropSupported()).isTrue();
        assertThat(source.isRotateSupported()).isTrue();
    }

    @Test
    @DisplayName("Constructor converts an RGB image to gray")
    void testRgbImageConstructor() {
        BufferedImage image = rgbImage(12, 10);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        assertThat(source.getWidth()).isEqualTo(12);
        assertThat(source.getHeight()).isEqualTo(10);
    }

    @Test
    @DisplayName("Constructor with crop rectangle converts an RGB image")
    void testCroppedConstructor() {
        BufferedImage image = rgbImage(20, 20);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image, 2, 2, 10, 10);
        assertThat(source.getWidth()).isEqualTo(10);
        assertThat(source.getHeight()).isEqualTo(10);
    }

    @Test
    @DisplayName("Constructor rejects a crop rectangle that exceeds the image bounds")
    void testCroppedConstructorOutOfBounds() {
        BufferedImage image = rgbImage(10, 10);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new BufferedImageLuminanceSource(image, 0, 0, 20, 20));
    }

    @Test
    @DisplayName("getRow returns the requested row and reuses a pre-allocated array")
    void testGetRow() {
        BufferedImage image = rgbImage(8, 8);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        byte[] row = source.getRow(0, null);
        assertThat(row).hasSize(8);
        byte[] reused = new byte[8];
        assertThat(source.getRow(0, reused)).isSameAs(reused);
    }

    @Test
    @DisplayName("getRow rejects an out-of-range row index")
    void testGetRowOutOfRange() {
        BufferedImage image = rgbImage(8, 8);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        assertThatIllegalArgumentException().isThrownBy(() -> source.getRow(20, null));
    }

    @Test
    @DisplayName("getRow allocates a larger buffer when the provided one is too small")
    void testGetRowTooSmallBuffer() {
        BufferedImage image = rgbImage(8, 8);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        byte[] tooSmall = new byte[2];
        byte[] row = source.getRow(0, tooSmall);
        assertThat(row).hasSize(8).isNotSameAs(tooSmall);
    }

    @Test
    @DisplayName("getMatrix returns the full luminance matrix")
    void testGetMatrix() {
        BufferedImage image = rgbImage(6, 4);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        byte[] matrix = source.getMatrix();
        assertThat(matrix).hasSize(6 * 4);
    }

    @Test
    @DisplayName("crop returns a new source with the cropped region")
    void testCrop() {
        BufferedImage image = grayImage(20, 20);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        LuminanceSource cropped = source.crop(2, 2, 8, 8);
        assertThat(cropped.getWidth()).isEqualTo(8);
        assertThat(cropped.getHeight()).isEqualTo(8);
    }

    @Test
    @DisplayName("rotateCounterClockwise returns a rotated source")
    void testRotateCounterClockwise() {
        BufferedImage image = grayImage(10, 8);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        LuminanceSource rotated = source.rotateCounterClockwise();
        assertThat(rotated).isNotNull();
    }

    @Test
    @DisplayName("rotateCounterClockwise45 returns a rotated source")
    void testRotateCounterClockwise45() {
        BufferedImage image = grayImage(10, 10);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        LuminanceSource rotated = source.rotateCounterClockwise45();
        assertThat(rotated).isNotNull();
    }
}
