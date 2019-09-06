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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.spring.boot.utils.BitMatrixUtils;

/**
 * 二维码工具类:生成和解析二维码；可处理有logo二维码
 * 
 * @author ： <a href="https://github.com/vindell">wandl</a>
 */
public class ZxingQrCodeTemplate {
	
	// 二维码边距
	private static final int QRCODE_MARGIN = 15;
	// 二维码尺寸
	public final int QRCODE_SIZE = 240;
	// LOGO宽度
	private static final int WIDTH = 45;
	// LOGO高度
	private static final int HEIGHT = 45;
	public static final String FORMAT_NAME = "png";
	/**
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, Image logo, OutputStream output) throws WriterException, IOException {
		qrcode(content, logo, output, false);
	}

	public void qrcode(String content, Image logo, OutputStream output)
			throws WriterException, IOException {
		qrcode(content, logo, output, QRCODE_SIZE, QRCODE_SIZE, QRCODE_MARGIN);
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 */
	public void qrcode(String content, Image logo, OutputStream output, int width, int height, int margin) throws WriterException, IOException {
		// 初始化二维码数据位阵
		BitMatrix bitMatrix = BitMatrixUtils.toMatrix(content, width, height, margin);
		// 生成图片
		BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		// 判断logo是否存在
		if (logo != null) {
			// 插入logo
			BitMatrixUtils.drawLogo(image, logo);
		}
		ImageIO.write(image, FORMAT_NAME, output);
	}

	public void qrcode(String content, int width, int height, OutputStream output) throws WriterException, IOException {
		// 初始化二维码数据位阵
		BitMatrix bitMatrix = BitMatrixUtils.toMatrix(content, width, height, QRCODE_MARGIN, ErrorCorrectionLevel.M);
		MatrixToImageWriter.writeToStream(bitMatrix, FORMAT_NAME, output);
	}

	public BufferedImage qrcode(String content, int width, int height, int margin) throws WriterException {
		// 初始化二维码数据位阵
		BitMatrix bitMatrix = BitMatrixUtils.toMatrix(content, width, height, margin, ErrorCorrectionLevel.M);
		// 图片处理
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 * @param output
	 * @throws Exception
	 */
	public void qrcode(String content, OutputStream output) throws Exception {
		qrcode(content, null, output, false);
	}
	  
	/**
	 * 显示二维码图片并编码为Base64
	 *
	 * @param content content
	 * @throws WriterException
	 * @throws IOException
	 */
	public String qrcodeBase64(String content) throws WriterException, IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		BufferedImage image = qrcode(content);
		// 输出二维码图片流
		ImageIO.write(image, "png", output);
		return "data:image/png;base64," + Base64.getEncoder().encodeToString(output.toByteArray());
	}

}
