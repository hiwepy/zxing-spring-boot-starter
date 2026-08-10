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

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ZxingQrCodeColorfullTemplate}.
 *
 * <p>Exercises every {@code qrcode} / {@code qrcodeBase64} overload and each
 * width-dependent colour branch (258/344/430/860/1290).</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ZxingQrCodeColorfullTemplate Tests")
class ZxingQrCodeColorfullTemplateTest {

    private static final String CONTENT = "https://github.com/easy-4-java/zxing-spring-boot-starter";

    private ZxingQrCodeColorfullTemplate template;

    @BeforeEach
    void setUp() {
        template = new ZxingQrCodeColorfullTemplate();
    }

    @Test
    @DisplayName("Size constants have documented values")
    void testConstants() {
        assertThat(ZxingQrCodeColorfullTemplate.QRCODE_258).isEqualTo(258);
        assertThat(ZxingQrCodeColorfullTemplate.QRCODE_344).isEqualTo(344);
        assertThat(ZxingQrCodeColorfullTemplate.QRCODE_430).isEqualTo(430);
        assertThat(ZxingQrCodeColorfullTemplate.QRCODE_860).isEqualTo(860);
        assertThat(ZxingQrCodeColorfullTemplate.QRCODE_1290).isEqualTo(1290);
    }

    @Test
    @DisplayName("qrcode(content) renders at the default size (left<=90 branch)")
    void testQrcodeDefault() throws Exception {
        BufferedImage image = template.qrcode(CONTENT);
        assertThat(image.getWidth()).isEqualTo(ZxingQrCodeColorfullTemplate.QRCODE_258);
        assertThat(image.getHeight()).isEqualTo(ZxingQrCodeColorfullTemplate.QRCODE_258);
    }

    @Test
    @DisplayName("qrcode(content, w, h) renders a custom size")
    void testQrcodeCustomSize() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 300, 300);
        assertThat(image.getWidth()).isEqualTo(300);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level) exercises the level overload")
    void testQrcodeWithLevel() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 200, 200, ErrorCorrectionLevel.H);
        assertThat(image.getWidth()).isEqualTo(200);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level) exercises the 344 width branch")
    void testQrcodeWidth344() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 344, 344, ErrorCorrectionLevel.M);
        assertThat(image.getWidth()).isEqualTo(344);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level) exercises the 430 width branch")
    void testQrcodeWidth430() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 430, 430, ErrorCorrectionLevel.M);
        assertThat(image.getWidth()).isEqualTo(430);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level) exercises the 860 width branch")
    void testQrcodeWidth860() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 860, 860, ErrorCorrectionLevel.M);
        assertThat(image.getWidth()).isEqualTo(860);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level) exercises the 1290 width branch")
    void testQrcodeWidth1290() throws Exception {
        BufferedImage image = template.qrcode(CONTENT, 1290, 1290, ErrorCorrectionLevel.M);
        assertThat(image.getWidth()).isEqualTo(1290);
    }

    @Test
    @DisplayName("qrcode(content, logo) embeds a logo")
    void testQrcodeWithLogo() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, logo);
        assertThat(image.getWidth()).isEqualTo(ZxingQrCodeColorfullTemplate.QRCODE_258);
    }

    @Test
    @DisplayName("qrcode(content, w, h, logo) embeds a logo at custom size")
    void testQrcodeCustomSizeWithLogo() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, 300, 300, logo);
        assertThat(image.getWidth()).isEqualTo(300);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, logo) embeds a logo with a level")
    void testQrcodeLevelWithLogo() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, 300, 300, ErrorCorrectionLevel.H, logo);
        assertThat(image.getWidth()).isEqualTo(300);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, logo, lw, lh, margin) embeds a scaled logo with margin")
    void testQrcodeScaledLogoWithMargin() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = template.qrcode(CONTENT, 300, 300, ErrorCorrectionLevel.H, logo, 40, 40, 6);
        assertThat(image.getWidth()).isEqualTo(300);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, logo, lw, lh, margin, output) writes a PNG stream")
    void testQrcodeLogoStream() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, 300, 300, ErrorCorrectionLevel.H, logo, 40, 40, 6, out);
        assertThat(out.size()).isGreaterThan(0);
        assertThat(out.toByteArray()[0] & 0xFF).isEqualTo(0x89);
    }

    @Test
    @DisplayName("qrcode(content, logo, output) writes a PNG stream")
    void testQrcodeDefaultLogoStream() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, logo, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcode(content, w, h, logo, output) writes a PNG stream")
    void testQrcodeCustomLogoStream() throws Exception {
        BufferedImage logo = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, 300, 300, logo, out);
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
        template.qrcode(CONTENT, 300, 300, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcode(content, w, h, level, output) writes a PNG stream")
    void testQrcodeLevelStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.qrcode(CONTENT, 300, 300, ErrorCorrectionLevel.H, out);
        assertThat(out.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("qrcodeBase64(content) returns a data URI prefixed string")
    void testQrcodeBase64Default() throws Exception {
        assertThat(template.qrcodeBase64(CONTENT)).startsWith("data:image/png;base64,");
    }

    @Test
    @DisplayName("qrcodeBase64(content, w, h) returns a data URI prefixed string")
    void testQrcodeBase64CustomSize() throws Exception {
        assertThat(template.qrcodeBase64(CONTENT, 300, 300)).startsWith("data:image/png;base64,");
    }

    @Test
    @DisplayName("qrcodeBase64(content, w, h, level) returns a data URI prefixed string")
    void testQrcodeBase64WithLevel() throws Exception {
        assertThat(template.qrcodeBase64(CONTENT, 300, 300, ErrorCorrectionLevel.H))
                .startsWith("data:image/png;base64,");
    }
}
