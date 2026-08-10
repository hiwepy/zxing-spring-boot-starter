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

import com.google.zxing.common.BitMatrix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link MatrixToImageWriter}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("MatrixToImageWriter Tests")
class MatrixToImageWriterTest {

    private BitMatrix sampleMatrix() {
        BitMatrix matrix = new BitMatrix(10, 8);
        // set a few bits to true so the image is not all-white
        for (int x = 0; x < 10; x++) {
            matrix.set(x, 0);
            matrix.set(x, 7);
        }
        matrix.set(0, 1);
        matrix.set(9, 6);
        return matrix;
    }

    @Test
    @DisplayName("toBufferedImage renders the matrix with matching dimensions")
    void testToBufferedImage() {
        BitMatrix matrix = sampleMatrix();
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        assertThat(image.getWidth()).isEqualTo(10);
        assertThat(image.getHeight()).isEqualTo(8);
        // corner bits are set (black)
        assertThat(image.getRGB(0, 0)).isEqualTo(0xFF000000);
        assertThat(image.getRGB(5, 5)).isEqualTo(0xFFFFFFFF); // white (unset)
    }

    @Test
    @DisplayName("toBufferedImage(matrix, size) resizes to the requested size")
    void testToBufferedImageWithSize() {
        BufferedImage image = MatrixToImageWriter.toBufferedImage(sampleMatrix(), 40);
        assertThat(image.getWidth()).isEqualTo(40);
        assertThat(image.getHeight()).isEqualTo(40);
    }

    @Test
    @DisplayName("updateBit trims and re-adds the margin")
    void testUpdateBit() {
        BitMatrix matrix = sampleMatrix();
        BitMatrix updated = MatrixToImageWriter.updateBit(matrix, 2);
        assertThat(updated.getWidth()).isGreaterThan(0);
        assertThat(updated.getHeight()).isGreaterThan(0);
    }

    @Test
    @DisplayName("zoomInImage resizes to the requested width/height")
    void testZoomInImage() {
        BufferedImage original = MatrixToImageWriter.toBufferedImage(sampleMatrix());
        BufferedImage zoomed = MatrixToImageWriter.zoomInImage(original, 30, 20);
        assertThat(zoomed.getWidth()).isEqualTo(30);
        assertThat(zoomed.getHeight()).isEqualTo(20);
    }

    @Test
    @DisplayName("writeToFile writes a readable PNG file")
    void testWriteToFile() throws IOException {
        Path tmp = Files.createTempFile("zxing-matrix-", ".png");
        MatrixToImageWriter.writeToFile(sampleMatrix(), "png", tmp.toFile());
        assertThat(Files.size(tmp)).isGreaterThan(0);
        Files.deleteIfExists(tmp);
    }

    @Test
    @DisplayName("writeToStream writes the PNG bytes")
    void testWriteToStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(sampleMatrix(), "png", out);
        assertThat(out.size()).isGreaterThan(0);
        // PNG magic header
        assertThat(out.toByteArray()[0] & 0xFF).isEqualTo(0x89);
    }
}
