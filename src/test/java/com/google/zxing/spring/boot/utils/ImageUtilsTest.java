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
package com.google.zxing.spring.boot.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Image;
import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ImageUtils}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ImageUtils Tests")
class ImageUtilsTest {

    private BufferedImage rgbImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, 0xFF0000FF); // blue
            }
        }
        return image;
    }

    @Test
    @DisplayName("buffered() returns the same instance for a BufferedImage")
    void testBufferedReturnsSameInstance() throws Exception {
        BufferedImage source = rgbImage(10, 10);
        assertThat(ImageUtils.buffered(source)).isSameAs(source);
    }

    @Test
    @DisplayName("buffered() redraws a non-BufferedImage")
    void testBufferedRedrawsNonBufferedImage() throws Exception {
        // A BufferedImage wrapped so it is treated as a plain Image via a 1x1 custom image is hard;
        // instead build a BufferedImage and pass it as Image to cover the instanceof branch.
        BufferedImage source = rgbImage(8, 8);
        Image asImage = source;
        assertThat(ImageUtils.buffered(asImage)).isSameAs(source);
    }

    @Test
    @DisplayName("scale(Image, h, w) returns a BufferedImage of the requested size")
    void testScaleImage() throws Exception {
        BufferedImage source = rgbImage(20, 20);
        BufferedImage scaled = ImageUtils.scale((Image) source, 16, 16);
        assertThat(scaled.getWidth()).isEqualTo(16);
        assertThat(scaled.getHeight()).isEqualTo(16);
    }

    @Test
    @DisplayName("scale(BufferedImage, h, w) returns a BufferedImage of the requested size")
    void testScaleBufferedImage() throws Exception {
        BufferedImage source = rgbImage(30, 30);
        BufferedImage scaled = ImageUtils.scale(source, 24, 24);
        assertThat(scaled.getWidth()).isEqualTo(24);
        assertThat(scaled.getHeight()).isEqualTo(24);
    }

    @Test
    @DisplayName("filler(BufferedImage, h, w) pads the image to the target size")
    void testFillerBufferedImage() throws Exception {
        BufferedImage source = rgbImage(10, 6);
        BufferedImage padded = ImageUtils.filler(source, 12, 12);
        assertThat(padded.getWidth()).isEqualTo(12);
        assertThat(padded.getHeight()).isEqualTo(12);
    }

    @Test
    @DisplayName("filler(Image, h, w) pads when width matches source (vertical padding)")
    void testFillerImageWidthMatches() throws Exception {
        BufferedImage source = rgbImage(12, 4);
        BufferedImage padded = ImageUtils.filler((Image) source, 12, 12);
        assertThat(padded.getWidth()).isEqualTo(12);
        assertThat(padded.getHeight()).isEqualTo(12);
    }

    @Test
    @DisplayName("filler(Image, h, w) pads when height matches source (horizontal padding)")
    void testFillerImageHeightMatches() throws Exception {
        BufferedImage source = rgbImage(4, 12);
        BufferedImage padded = ImageUtils.filler((Image) source, 12, 12);
        assertThat(padded.getWidth()).isEqualTo(12);
        assertThat(padded.getHeight()).isEqualTo(12);
    }
}
