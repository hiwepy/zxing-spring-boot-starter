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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.spring.boot.utils.BitMatrixUtils;
import com.google.zxing.spring.boot.utils.ImageUtils;

public class MatrixToImageWriter {
	private static final int LOGO_WIDTH = 64;
	private static final int LOGO_HEIGHT = 64;
	private static final int LOGO_HALF_WIDTH = LOGO_WIDTH / 2;
	
	private static final int QRCODE_MARGIN = 5;

	public static void encode(String content, int width, int height, String srcImagePath, String destImagePath) {
		try {
			ImageIO.write(genBarcode(content, width, height, srcImagePath), "jpg", new File(destImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage genBarcode(String content, int width, int height, String logPath)
			throws WriterException, IOException {
		
		// 生成二维码
		//Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
		//hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		//hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		//BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hint);
		
		BitMatrix matrix = BitMatrixUtils.bitMatrix(content, width, height, ErrorCorrectionLevel.H);
		
		File file = new File(logPath);
		BufferedImage logo = ImageIO.read(file);
		BufferedImage logoImage = ImageUtils.scale(logo, LOGO_WIDTH, LOGO_HEIGHT);
		//BufferedImage logoImage = scale(logPath, LOGO_WIDTH, LOGO_HEIGHT, true);
		int[][] srcPixels = new int[LOGO_WIDTH][LOGO_HEIGHT];
		for (int i = 0; i < logoImage.getWidth(); i++) {
			for (int j = 0; j < logoImage.getHeight(); j++) {
				srcPixels[i][j] = logoImage.getRGB(i, j);
			}
		}
		
		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 左上角颜色,根据自己需要调整颜色范围和颜色
				if (x > 0 && x < halfW - LOGO_HALF_WIDTH && y > 0 && y < halfW - LOGO_HALF_WIDTH) {
					Color color = new Color(231, 144, 56);
					int colorInt = color.getRGB();
					pixels[y * width + x] = matrix.get(x, y) ? colorInt : 16777215;
				}
				// 读取图片
				else if (x > halfW - LOGO_HALF_WIDTH && x < halfW + LOGO_HALF_WIDTH && y > halfH - LOGO_HALF_WIDTH
						&& y < halfH + LOGO_HALF_WIDTH) {
					pixels[y * width + x] = srcPixels[x - halfW + LOGO_HALF_WIDTH][y - halfH + LOGO_HALF_WIDTH];
				} else if ((x > halfW - LOGO_HALF_WIDTH - QRCODE_MARGIN && x < halfW - LOGO_HALF_WIDTH + QRCODE_MARGIN
						&& y > halfH - LOGO_HALF_WIDTH - QRCODE_MARGIN && y < halfH + LOGO_HALF_WIDTH + QRCODE_MARGIN)
						|| (x > halfW + LOGO_HALF_WIDTH - QRCODE_MARGIN && x < halfW + LOGO_HALF_WIDTH + QRCODE_MARGIN
								&& y > halfW - LOGO_HALF_WIDTH - QRCODE_MARGIN
								&& y < halfH + LOGO_HALF_WIDTH + QRCODE_MARGIN)
						|| (x > halfW - LOGO_HALF_WIDTH - QRCODE_MARGIN && x < halfW + LOGO_HALF_WIDTH + QRCODE_MARGIN
								&& y > halfH - LOGO_HALF_WIDTH - QRCODE_MARGIN
								&& y < halfH - LOGO_HALF_WIDTH + QRCODE_MARGIN)
						|| (x > halfW - LOGO_HALF_WIDTH - QRCODE_MARGIN && x < halfW + LOGO_HALF_WIDTH + QRCODE_MARGIN
								&& y > halfH + LOGO_HALF_WIDTH - QRCODE_MARGIN
								&& y < halfH + LOGO_HALF_WIDTH + QRCODE_MARGIN)) {
					pixels[y * width + x] = 0xfffffff;
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

	public static void main(String[] args) throws UnsupportedEncodingException {
		// 依次为内容(不支持中文),宽,长,中间图标路径,储存路径
		String logoPath = "D:\\workspaces\\workspace-jeebiz\\workspace-jeebiz-boot-starters\\spring-boot-starter-zxing\\logo2.jpg"; // logo文件名。

		MatrixToImageWriter.encode("http://www.baidu.com/", 258, 258, logoPath, "F:\\2013-01.jpg");

	}
}