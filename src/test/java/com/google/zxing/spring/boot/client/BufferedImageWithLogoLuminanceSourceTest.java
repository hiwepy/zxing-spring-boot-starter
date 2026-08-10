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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link BufferedImageWithLogoLuminanceSource}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("BufferedImageWithLogoLuminanceSource Tests")
class BufferedImageWithLogoLuminanceSourceTest {

    private BufferedImage rgbImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, 0xFFFFFFFF); // white
            }
        }
        return image;
    }

    @Test
    @DisplayName("Full-image constructor initialises and exposes dimensions")
    void testFullConstructor() {
        BufferedImage image = rgbImage(10, 8);
        BufferedImageWithLogoLuminanceSource source = new BufferedImageWithLogoLuminanceSource(image);
        assertThat(source.getWidth()).isEqualTo(10);
        assertThat(source.getHeight()).isEqualTo(8);
        assertThat(source.isRotateSupported()).isTrue();
    }

    @Test
    @DisplayName("Cropped constructor initialises with the crop region")
    void testCroppedConstructor() {
        BufferedImage image = rgbImage(20, 20);
        BufferedImageWithLogoLuminanceSource source =
                new BufferedImageWithLogoLuminanceSource(image, 2, 2, 10, 10);
        assertThat(source.getWidth()).isEqualTo(10);
        assertThat(source.getHeight()).isEqualTo(10);
    }

    @Test
    @DisplayName("Cropped constructor rejects a region exceeding the image bounds")
    void testCroppedConstructorOutOfBounds() {
        BufferedImage image = rgbImage(10, 10);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new BufferedImageWithLogoLuminanceSource(image, 0, 0, 20, 20));
    }

    @Test
    @DisplayName("getRow returns a byte array sized to the width")
    void testGetRow() {
        BufferedImage image = rgbImage(8, 8);
        BufferedImageWithLogoLuminanceSource source = new BufferedImageWithLogoLuminanceSource(image);
        assertThat(source.getRow(0, null)).hasSize(8);
    }

    @Test
    @DisplayName("getRow rejects an out-of-range row index")
    void testGetRowOutOfRange() {
        BufferedImage image = rgbImage(8, 8);
        BufferedImageWithLogoLuminanceSource source = new BufferedImageWithLogoLuminanceSource(image);
        assertThatIllegalArgumentException().isThrownBy(() -> source.getRow(99, null));
    }

    @Test
    @DisplayName("getRow reuses a sufficiently large provided array")
    void testGetRowReusesArray() {
        BufferedImage image = rgbImage(8, 8);
        BufferedImageWithLogoLuminanceSource source = new BufferedImageWithLogoLuminanceSource(image);
        byte[] reused = new byte[8];
        assertThat(source.getRow(0, reused)).isSameAs(reused);
    }

    @Test
    @DisplayName("getMatrix returns the full luminance matrix")
    void testGetMatrix() {
        BufferedImage image = rgbImage(6, 4);
        BufferedImageWithLogoLuminanceSource source = new BufferedImageWithLogoLuminanceSource(image);
        assertThat(source.getMatrix()).hasSize(6 * 4);
    }
}
