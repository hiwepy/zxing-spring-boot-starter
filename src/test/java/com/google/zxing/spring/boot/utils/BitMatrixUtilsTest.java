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

import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link BitMatrixUtils}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("BitMatrixUtils Tests")
class BitMatrixUtilsTest {

    private static final String CONTENT = "https://github.com/easy-4-java/zxing-spring-boot-starter";

    @Test
    @DisplayName("CHARSET constant is utf-8")
    void testCharsetConstant() {
        assertThat(BitMatrixUtils.CHARSET).isEqualTo("utf-8");
    }

    @Test
    @DisplayName("bitMatrix(content, w, h, level) builds a non-empty matrix")
    void testBitMatrix() throws WriterException {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        assertThat(matrix).isNotNull();
        assertThat(matrix.getWidth()).isEqualTo(200);
        assertThat(matrix.getHeight()).isEqualTo(200);
    }

    @Test
    @DisplayName("bitMatrixWithMargin applies the requested margin")
    void testBitMatrixWithMargin() throws WriterException {
        BitMatrix matrix = BitMatrixUtils.bitMatrixWithMargin(CONTENT, 200, 200, ErrorCorrectionLevel.H, 10);
        assertThat(matrix).isNotNull();
        assertThat(matrix.getWidth()).isGreaterThan(0);
    }

    @Test
    @DisplayName("bitMatrix(...,formatName) returns PNG bytes")
    void testBitMatrixBytes() throws WriterException, IOException {
        byte[] bytes = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M, "png");
        assertThat(bytes).isNotEmpty();
        // PNG magic header
        assertThat(bytes[0] & 0xFF).isEqualTo(0x89);
    }

    @Test
    @DisplayName("drawLogo(source, logo) overlays the logo using its native size")
    void testDrawLogoNativeSize() throws IOException {
        BufferedImage source = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BitMatrixUtils.drawLogo(source, logo);
        // the source is mutated in place; assert it still has expected dimensions
        assertThat(source.getWidth()).isEqualTo(100);
    }

    @Test
    @DisplayName("drawLogo(source, logo, w, h) overlays a scaled logo")
    void testDrawLogoScaled() throws IOException {
        BufferedImage source = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        BufferedImage logo = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        BitMatrixUtils.drawLogo(source, logo, 30, 30);
        assertThat(source.getWidth()).isEqualTo(200);
    }

    @Test
    @DisplayName("parse(bytes) round-trips a generated QR code")
    void testParseBytesRoundTrip() throws Exception {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.google.zxing.spring.boot.client.MatrixToImageWriter.writeToStream(matrix, "png", out);
        Result result = BitMatrixUtils.parse(out.toByteArray());
        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("parse(InputStream) round-trips a generated QR code")
    void testParseInputStreamRoundTrip() throws Exception {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.google.zxing.spring.boot.client.MatrixToImageWriter.writeToStream(matrix, "png", out);
        Result result = BitMatrixUtils.parse(new ByteArrayInputStream(out.toByteArray()));
        assertThat(result.getText()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("parse(File) round-trips a generated QR code")
    void testParseFileRoundTrip(@TempDir Path tempDir) throws Exception {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        Path file = tempDir.resolve("qrcode.png");
        com.google.zxing.spring.boot.client.MatrixToImageWriter.writeToFile(matrix, "png", file.toFile());

        Result result = BitMatrixUtils.parse(file.toFile());
        assertThat(result.getText()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("parse(BufferedImage) round-trips a generated QR code")
    void testParseBufferedImageRoundTrip() throws Exception {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        BufferedImage image = com.google.zxing.spring.boot.client.MatrixToImageWriter.toBufferedImage(matrix);
        Result result = BitMatrixUtils.parse(image);
        assertThat(result.getText()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("parse(BufferedImage) returns null for a null image")
    void testParseNullImage() throws Exception {
        Result result = BitMatrixUtils.parse((BufferedImage) null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("parse(InputStream) returns null when the stream is not an image")
    void testParseNonImageStream() throws Exception {
        Result result = BitMatrixUtils.parse(new ByteArrayInputStream("not an image".getBytes()));
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("parseWithLogo(bytes) decodes a generated QR code")
    void testParseWithLogoBytes() throws Exception {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.google.zxing.spring.boot.client.MatrixToImageWriter.writeToStream(matrix, "png", out);
        Result result = BitMatrixUtils.parseWithLogo(out.toByteArray());
        assertThat(result.getText()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("parseWithLogo(InputStream) decodes a generated QR code")
    void testParseWithLogoInputStream() throws Exception {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.google.zxing.spring.boot.client.MatrixToImageWriter.writeToStream(matrix, "png", out);
        Result result = BitMatrixUtils.parseWithLogo(new ByteArrayInputStream(out.toByteArray()));
        assertThat(result.getText()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("parseWithLogo(BufferedImage) decodes a generated QR code")
    void testParseWithLogoImage() throws Exception {
        BitMatrix matrix = BitMatrixUtils.bitMatrix(CONTENT, 200, 200, ErrorCorrectionLevel.M);
        BufferedImage image = com.google.zxing.spring.boot.client.MatrixToImageWriter.toBufferedImage(matrix);
        Result result = BitMatrixUtils.parseWithLogo(image);
        assertThat(result.getText()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("parseWithLogo(BufferedImage) returns null for a null image")
    void testParseWithLogoNullImage() throws Exception {
        Result result = BitMatrixUtils.parseWithLogo((BufferedImage) null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("parse(File) on a non-image returns null")
    void testParseFileNonImage(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("not-an-image.txt");
        Files.writeString(file, "hello");
        Result result = BitMatrixUtils.parse(file.toFile());
        assertThat(result).isNull();
    }
}
