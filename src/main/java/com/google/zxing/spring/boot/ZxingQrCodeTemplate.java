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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.spring.boot.client.MatrixToImageWriter;
import com.google.zxing.spring.boot.utils.BitMatrixUtils;

/**
 * 二维码工具类:生成和解析二维码；可处理有logo二维码
 * 
 * @author ： <a href="https://github.com/hiwepy">wandl</a>
 */
public class ZxingQrCodeTemplate {

	// 1290*1290 860*860 430*430 344*344 258*258
	public static final int QRCODE_258 = 258; // 258*258
	public static final int QRCODE_344 = 344; // 344*344
	public static final int QRCODE_430 = 430; // 430*430
	public static final int QRCODE_860 = 860; // 860*860
	public static final int QRCODE_1290 = 1290; // 1290*1290
	private static final int LOGO_WIDTH = 48; // LOGO宽度
	private static final int LOGO_HEIGHT = 48; // LOGO高度
	private static final String FORMAT_NAME = "png";
	private static final String BASE64_PREFIX = "data:image/png;base64,";

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, Image logo, OutputStream output) throws WriterException, IOException {
		qrcode(content, QRCODE_258, QRCODE_258, ErrorCorrectionLevel.M, logo, output);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, int width, int height, Image logo, OutputStream output)
			throws WriterException, IOException {
		qrcode(content, width, height, ErrorCorrectionLevel.M, logo, output);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, int width, int height, ErrorCorrectionLevel level, Image logo,
			OutputStream output) throws WriterException, IOException {
		// 二维码图片
		BufferedImage image = qrcode(content, width, height, level, logo);
		// 输出二维码图片流
		ImageIO.write(image, FORMAT_NAME, output);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public BufferedImage qrcode(String content, Image logo) throws WriterException, IOException {
		return qrcode(content, QRCODE_258, QRCODE_258, ErrorCorrectionLevel.M, logo);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public BufferedImage qrcode(String content, int width, int height, Image logo) throws WriterException, IOException {
		return qrcode(content, width, height, ErrorCorrectionLevel.M, logo);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public BufferedImage qrcode(String content, int width, int height, ErrorCorrectionLevel level, Image logo)
			throws WriterException, IOException {
		return qrcode(content, width, height, level, logo, LOGO_WIDTH, LOGO_HEIGHT);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public BufferedImage qrcode(String content, int width, int height, ErrorCorrectionLevel level, Image logo,
			int logoWidth, int logoHeight) throws WriterException, IOException {
		// 初始化二维码数据位阵
		BitMatrix matrix = BitMatrixUtils.bitMatrix(content, width, height, level);
		// 生成图片
		BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
		// 判断logo是否存在
		if (logo != null) {
			// 插入logo
			BitMatrixUtils.drawLogo(image, logo, logoWidth, logoHeight);
		}
		return image;
	}

	/*
	 * 生成二维码
	 */
	public BufferedImage qrcode(String content) throws WriterException, IOException {
		return qrcode(content, QRCODE_258, QRCODE_258, ErrorCorrectionLevel.M);
	}

	/*
	 * 生成二维码
	 */
	public BufferedImage qrcode(String content, int width, int height) throws WriterException, IOException {
		return qrcode(content, width, height, ErrorCorrectionLevel.M);
	}

	/*
	 * 生成二维码
	 */
	public BufferedImage qrcode(String content, int width, int height, ErrorCorrectionLevel level)
			throws WriterException {
		// 初始化二维码数据位阵
		BitMatrix bitMatrix = BitMatrixUtils.bitMatrix(content, width, height, ErrorCorrectionLevel.M);
		// 图片处理
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	/*
	 * 生成二维码
	 */
	public void qrcode(String content, OutputStream output) throws WriterException, IOException {
		qrcode(content, QRCODE_258, QRCODE_258, ErrorCorrectionLevel.M, output);
	}

	/*
	 * 生成二维码
	 */
	public void qrcode(String content, int width, int height, OutputStream output) throws WriterException, IOException {
		qrcode(content, width, height, ErrorCorrectionLevel.M, output);
	}

	/*
	 * 生成二维码
	 */
	public void qrcode(String content, int width, int height, ErrorCorrectionLevel level, OutputStream output)
			throws WriterException, IOException {
		ImageIO.write(qrcode(content, width, height, level), FORMAT_NAME, output);
	}

	/*
	 * 生成二维码并编码为Base64
	 */
	public String qrcodeBase64(String content) throws WriterException, IOException {
		return qrcodeBase64(content, QRCODE_258, QRCODE_258, ErrorCorrectionLevel.M);
	}

	/*
	 * 生成二维码并编码为Base64
	 */
	public String qrcodeBase64(String content, int width, int height) throws WriterException, IOException {
		return qrcodeBase64(content, width, height, ErrorCorrectionLevel.M);
	}

	/*
	 * 生成二维码并编码为Base64
	 */
	public String qrcodeBase64(String content, int width, int height, ErrorCorrectionLevel level)
			throws WriterException, IOException {
		try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			// 二维码图片
			BufferedImage image = qrcode(content, width, height, level);
			// 输出二维码图片流
			ImageIO.write(image, FORMAT_NAME, output);
			return BASE64_PREFIX + Base64.getEncoder().encodeToString(output.toByteArray());
		}
	}

}
