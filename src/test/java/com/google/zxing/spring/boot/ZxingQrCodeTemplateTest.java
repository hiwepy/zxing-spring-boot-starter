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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ZxingQrCodeTemplate}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ZxingQrCodeTemplate Tests")
class ZxingQrCodeTemplateTest {

    private static final String CONTENT = "https://github.com/easy-4-java/zxing-spring-boot-starter";

    private ZxingQrCodeTemplate template;

    @BeforeEach
    void setUp() {
        template = new ZxingQrCodeTemplate();
    }

    @Test
    @DisplayName("Size constants have documented values")
    void testConstants() {
        assertThat(ZxingQrCodeTemplate.QRCODE_258).isEqualTo(258);
        assertThat(ZxingQrCodeTemplate.QRCODE_344).isEqualTo(344);
        assertThat(ZxingQrCodeTemplate.QRCODE_430).isEqualTo(430);
        assertThat(ZxingQrCodeTemplate.QRCODE_860).isEqualTo(860);
        assertThat(ZxingQrCodeTemplate.QRCODE_1290).isEqualTo(1290);
    }

    @Test
    @DisplayName("qrcode(content) returns a default-size image")
    void testQrcodeDefault() throws Exception {
        BufferedImage image = template.qrcode(CONTENT);
        assertThat(image.getWidth()).isEqualTo(ZxingQrCodeTemplate.QRCODE_258);
        assertThat(image.getHeight()).isEqualTo(ZxingQrCodeTemplate.QRCODE_258);
    }

    @Test
    @DisplayName("qrcode(content, w, h) returns a custom-size image")
    void testQrcodeCustomSize() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 300, 300);
        assertThat(image.getWidth()).isEqualTo(300);
        assertThat(image.getHeight()).isEqualTo(300);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level) returns an image")
    void testQrcodeWithLevel() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 200, 200,
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);
        assertThat(image.getWidth()).isEqualTo(200);
    }

    @Test
    @DisplayName("qrcode(content, logo) embeds a logo")
    void testQrcodeWithLogo() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, logo);
        assertThat(image.getWidth()).isEqualTo(ZxingQrCodeTemplate.QRCODE_258);
    }

    @Test
    @DisplayName("qrcode(content, w, h, logo) embeds a logo at a custom size")
    void testQrcodeCustomSizeWithLogo() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, 300, 300, logo);
        assertThat(image.getWidth()).isEqualTo(300);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, logo) embeds a logo with a correction level")
    void testQrcodeLevelWithLogo() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, 200, 200,
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H, logo);
        assertThat(image.getWidth()).isEqualTo(200);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, logo, lw, lh) embeds a scaled logo")
    void testQrcodeScaledLogo() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, 200, 200,
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H, logo, 30, 30);
        assertThat(image.getWidth()).isEqualTo(200);
    }

    @Test
    @DisplayName("qrcode(content, logo, output) writes a PNG stream")
    void testQrcodeLogoStream() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, logo, out);
        assertThat(out.size()).isGreaterThan(0);
        assertThat(out.toByteArray()[0] & 0xFF).isEqualTo(0x89);
    }

    @Test
    @DisplayName("qrcode(content, w, h, logo, output) writes a PNG stream")
    void testQrcodeCustomLogoStream() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, 200, 200, logo, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, logo, output) writes a PNG stream")
    void testQrcodeLevelLogoStream() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, 200, 200,
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H, logo, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcode(content, output) writes a PNG stream")
    void testQrcodeStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcode(content, w, h, output) writes a PNG stream")
    void testQrcodeCustomStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, 200, 200, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, output) writes a PNG stream")
    void testQrcodeLevelStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, 200, 200,
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcodeBase64(content) returns a data URI prefixed string")
    void testQrcodeBase64Default() throws Exception {
        String base64 = template.qrcodeBase64(CONTENT);
        assertThat(base64).startsWith("data:image/png;base64,");
    }

    @Test
    @DisplayName("qrcodeBase64(content, w, h) returns a data URI prefixed string")
    void testQrcodeBase64CustomSize() throws Exception {
        String base64 = template.qrcodeBase64(CONTENT, 200, 200);
        assertThat(base64).startsWith("data:image/png;base64,");
    }

    @Test
    @DisplayName("qrcodeBase64(content, w, h, level) returns a data URI prefixed string")
    void testQrcodeBase64WithLevel() throws Exception {
        String base64 = template.qrcodeBase64(CONTENT, 200, 200,
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);
        assertThat(base64).startsWith("data:image/png;base64,");
    }
}
