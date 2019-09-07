/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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

import java.awt.Color;
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
import com.google.zxing.spring.boot.utils.BitMatrixUtils;
import com.google.zxing.spring.boot.utils.ImageUtils;

/**
 * 二维码工具类:生成和解析二维码；可处理有logo二维码
 * 
 * @author ： <a href="https://github.com/vindell">wandl</a>
 */
public class ZxingQrCodeColorfullTemplate {

	// 1290*1290 860*860 430*430 344*344 258*258
	public static final int QRCODE_258 = 258; // 258*258
	public static final int QRCODE_344 = 344; // 344*344
	public static final int QRCODE_430 = 430; // 430*430
	public static final int QRCODE_860 = 860; // 860*860
	public static final int QRCODE_1290 = 1290; // 1290*1290
	private static final int LOGO_WIDTH = 48; // LOGO宽度
	private static final int LOGO_HEIGHT = 48; // LOGO高度
	private static final int LOGO_MARGIN = 5; // LOGO边距
	private static final String FORMAT_NAME = "png";
	private static final String BASE64_PREFIX = "data:image/png;base64,";

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, Image logo, OutputStream output) throws WriterException, IOException {
		qrcode(content, QRCODE_258, QRCODE_258, ErrorCorrectionLevel.M, logo, LOGO_WIDTH, LOGO_HEIGHT, LOGO_MARGIN,
				output);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, int width, int height, Image logo, OutputStream output)
			throws WriterException, IOException {
		qrcode(content, width, height, ErrorCorrectionLevel.M, logo, LOGO_WIDTH, LOGO_HEIGHT, LOGO_MARGIN, output);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, int width, int height, ErrorCorrectionLevel level, Image logo, int logoWidth,
			int logoHeight, int logoMargin, OutputStream output) throws WriterException, IOException {
		// 二维码图片
		BufferedImage image = qrcode(content, width, height, level, logo, logoWidth, logoHeight, logoMargin);
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

	public BufferedImage qrcode(String content, int width, int height, ErrorCorrectionLevel level, Image logo)
			throws WriterException, IOException {
		return qrcode(content, width, height, level, logo, LOGO_WIDTH, LOGO_HEIGHT, LOGO_MARGIN);
	}

	/*
	 * 生成二维码(内嵌LOGO)
	 */
	public BufferedImage qrcode(String content, int width, int height, ErrorCorrectionLevel level, Image logo,
			int logoWidth, int logoHeight, int logoMargin) throws WriterException, IOException {

		// 初始化二维码数据位阵
		BitMatrix matrix = BitMatrixUtils.bitMatrix(content, width, height, level);

		// 缩放LOGO
		BufferedImage logoImage = ImageUtils.scale(logo, logoWidth, logoHeight);
		int[][] srcPixels = new int[logoWidth][logoHeight];
		for (int i = 0; i < logoImage.getWidth(); i++) {
			for (int j = 0; j < logoImage.getHeight(); j++) {
				srcPixels[i][j] = logoImage.getRGB(i, j);
			}
		}

		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int logo_half_width = logoWidth / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 90 120 150 300 450
				// 258 344 430 860 1290
				int left = 90;
				if (width >= 1290) {
					left = 450;
				} else if (860 <= width && width < 1290) {
					left = 300;
				} else if (430 <= width && width < 860) {
					left = 150;
				} else if (344 <= width && width < 430) {
					left = 120;
				} else {
					left = 90;
				}
				// 左上角颜色,根据自己需要调整颜色范围和颜色
				if (x > 0 && x < left && y > 0 && y < left) {
					Color color = new Color(231, 144, 56);
					int colorInt = color.getRGB();
					pixels[y * width + x] = matrix.get(x, y) ? colorInt : 16777215;
				}
				// 读取图片
				else if (x > halfW - logo_half_width && x < halfW + logo_half_width && y > halfH - logo_half_width
						&& y < halfH + logo_half_width) {
					pixels[width * y + x] = srcPixels[x - halfW + logo_half_width][y - halfH + logo_half_width];
				} else if ((x > halfW - logo_half_width - logoMargin && x < halfW - logo_half_width + logoMargin
						&& y > halfH - logo_half_width - logoMargin && y < halfH + logo_half_width + logoMargin)
						|| (x > halfW + logo_half_width - logoMargin && x < halfW + logo_half_width + logoMargin
								&& y > halfW - logo_half_width - logoMargin && y < halfH + logo_half_width + logoMargin)
						|| (x > halfW - logo_half_width - logoMargin && x < halfW + logo_half_width + logoMargin
								&& y > halfH - logo_half_width - logoMargin && y < halfH - logo_half_width + logoMargin)
						|| (x > halfW - logo_half_width - logoMargin && x < halfW + logo_half_width + logoMargin
								&& y > halfH + logo_half_width - logoMargin
								&& y < halfH + logo_half_width + logoMargin)) {
					pixels[width * y + x] = 0xfffffff;
					// 在图片四周形成边框
				} else {
					// 二维码颜色
					int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
					int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
					int num3 = (int) (162 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
					Color color = new Color(num1, num2, num3);
					int colorInt = color.getRGB();
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * width + x] = matrix.get(x, y) ? colorInt : 16777215;
					// 0x000000:0xffffff
				}
			}
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, width, height, pixels);
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
		BitMatrix matrix = BitMatrixUtils.bitMatrix(content, width, height, level);
		// 二维矩阵转为一维像素数组
		int[] pixels = new int[width * height];
		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 90 120 150 300 450
				// 258 344 430 860 1290
				int left = 90;
				if (width >= 1290) {
					left = 450;
				} else if (860 <= width && width < 1290) {
					left = 300;
				} else if (430 <= width && width < 860) {
					left = 150;
				} else if (344 <= width && width < 430) {
					left = 120;
				} else {
					left = 90;
				}
				// 左上角颜色,根据自己需要调整颜色范围和颜色
				if (x > 0 && x < left && y > 0 && y < left) {
					Color color = new Color(231, 144, 56);
					pixels[y * width + x] = matrix.get(x, y) ? color.getRGB() : 16777215;
				} else {
					// 二维码颜色
					int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
					int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
					int num3 = (int) (162 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
					Color color = new Color(num1, num2, num3);
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * width + x] = matrix.get(x, y) ? color.getRGB() : 16777215;
					// 0x000000:0xffffff
				}
			}
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, width, height, pixels);
		return image;
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
		// 二维码图片
		BufferedImage image = qrcode(content, width, height, level);
		// 输出二维码图片流
		ImageIO.write(image, FORMAT_NAME, output);
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
