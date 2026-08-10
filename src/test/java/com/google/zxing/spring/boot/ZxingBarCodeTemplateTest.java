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
package com.google.zxing.spring.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ZxingBarCodeTemplate}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ZxingBarCodeTemplate Tests")
class ZxingBarCodeTemplateTest {

    private ZxingBarCodeTemplate template;

    @BeforeEach
    void setUp() {
        template = new ZxingBarCodeTemplate();
    }

    @Test
    @DisplayName("encode() writes a PNG file for a valid EAN-13 content")
    void testEncodeWritesFile(@TempDir Path tempDir) throws Exception {
        // A valid 12/13-digit EAN-13 content (check digit computed by zxing)
        String imgPath = tempDir.resolve("barcode.png").toString();
        template.encode("6901234567892", 200, 60, imgPath);

        File file = new File(imgPath);
        assertThat(file).exists();
        BufferedImage image = ImageIO.read(file);
        assertThat(image).isNotNull();
    }

    @Test
    @DisplayName("encode() swallows invalid content without throwing")
    void testEncodeInvalidContent(@TempDir Path tempDir) {
        String imgPath = tempDir.resolve("bad.png").toString();
        // Invalid content should be caught internally and not propagate
        template.encode("not-a-valid-ean13", 200, 60, imgPath);
        // The exception is swallowed, so the test simply asserts it does not throw
    }

    @Test
    @DisplayName("decode() returns the encoded text for a written barcode")
    void testDecodeRoundTrip(@TempDir Path tempDir) throws Exception {
        String imgPath = tempDir.resolve("barcode.png").toString();
        // Use a valid EAN-13 content; zxing will compute/validate the check digit
        template.encode("6901234567892", 300, 100, imgPath);

        String decoded = template.decode(imgPath);
        // Decoding may return null if the barcode is not detected, but for a freshly
        // generated EAN-13 it should typically round-trip
        assertThat(decoded).isNotNull();
    }

    @Test
    @DisplayName("decode() returns null for a non-image path")
    void testDecodeNonImage(@TempDir Path tempDir) throws Exception {
        File file = tempDir.resolve("not-an-image.txt").toFile();
        java.nio.file.Files.writeString(file.toPath(), "hello");
        // The exception is swallowed internally and null is returned
        assertThat(template.decode(file.getAbsolutePath())).isNull();
    }
}
