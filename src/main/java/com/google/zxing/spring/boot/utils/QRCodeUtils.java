package com.google.zxing.spring.boot.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.spring.boot.client.MatrixToImageWriter;

/**
 * 
 * @className: QRCodeUtils
 * @description: 二维码工具类:生成和解析二维码；可处理有logo二维码
 * @author : wandalong
 * @date : 下午12:57:56 2015-4-17
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class QRCodeUtils {

	// 二维码尺寸
	public static final int QRCODE_SIZE = 240;

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 * @param logo
	 * @param output
	 * @throws Exception
	 */
	public static void toQrcode(String content, Image logo, OutputStream output) throws Exception {
		toQrcode(content, logo, output, false);
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 * @param logo
	 * @param output
	 * @param needCompress
	 * @throws Exception
	 */
	public static void toQrcode(String content, Image logo, OutputStream output, boolean needCompress)
			throws Exception {
		// 初始化二维码数据位阵
		BitMatrix bitMatrix = BitMatrixUtils.toMatrix(content, QRCODE_SIZE, QRCODE_SIZE);
		// 生成图片
		BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		// 判断logo是否存在
		if (logo != null) {
			// 插入logo
			BitMatrixUtils.drawLogo(image, logo, needCompress);
		}
		ImageIO.write(image, BitMatrixUtils.FORMAT_NAME, output);
	}

	/**
	 * @param content
	 * @param width
	 * @param height
	 * @param output
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void toQrcode(String content, int width, int height, OutputStream output)
			throws WriterException, IOException {
		// 初始化二维码数据位阵
		BitMatrix bitMatrix = BitMatrixUtils.toMatrix(content, width, height);
		MatrixToImageWriter.writeToStream(bitMatrix, BitMatrixUtils.FORMAT_NAME, output);
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 * @param output
	 * @throws Exception
	 */
	public static void toQrcode(String content, OutputStream output) throws Exception {
		toQrcode(content, null, output, false);
	}

	public static String getDecodeText(BufferedImage image) {
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		try {
			result = new MultiFormatReader().decode(bitmap);
		} catch (ReaderException re) {
			return re.toString();
		}
		return String.valueOf(result.getText());
	}

}
