package com.google.zxing.spring.boot.utils;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.spring.boot.client.BufferedImageLuminanceSource;
import com.google.zxing.spring.boot.client.BufferedImageWithLogoLuminanceSource;
import com.google.zxing.spring.boot.client.MatrixToImageWriter;

public class BitMatrixUtils {

	public static final String CHARSET = "utf-8";
	private static MultiFormatWriter mutiWriter = new MultiFormatWriter();
	
	public static void drawLogo(BufferedImage source, Image logo) throws IOException{
		// 绘制LOGO
		drawLogo(source, logo, logo.getWidth(null), logo.getHeight(null));
	}
	
	public static void drawLogo(BufferedImage source, Image logo, int logoWidth, int logoHeight) throws IOException {
		// 缩放LOGO
		BufferedImage scaleImage = ImageUtils.scale(logo, logoWidth, logoHeight);
		// 绘制LOGO
		Graphics2D graph = source.createGraphics();
		int x = (source.getWidth() - logoWidth) / 2;
		int y = (source.getHeight() - logoHeight) / 2;
		graph.drawImage(scaleImage, x, y, logoWidth, logoHeight, null);
		Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}
	
	public static BitMatrix bitMatrix(String content, int width, int height, ErrorCorrectionLevel level) throws WriterException {
		// 用于设置QR二维码参数
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别——这里选择最高H级别
		hints.put(EncodeHintType.ERROR_CORRECTION, level);
		/*
		 * 指定编码格式:注意这里utf-8一定要小写 这样就可以解决手机不能识别的问题，而且也能支持中文。
		 * 至于原因，查看了源代码后，发现使用“UTF-8”，会在文本编码前添加一段ECI(扩充解释Extended Channel Interpretation)
		 * 编码，就是这段编码导致手机不能解析。如果使用小写"utf-8"会使这个ECI判断失效而不影响内容编码方式。 至于详细的ECI解释，可以看《QRCode
		 * 编码解码标准》
		 */
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 0);
		// 写入字节矩阵；参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
		BitMatrix byteMatrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height , hints); // 写入字节矩阵。
		// 返回字节矩阵
		return byteMatrix;
	}

	
	public static BitMatrix bitMatrixWithMargin(String content, int width, int height, ErrorCorrectionLevel level, int margin) throws WriterException {
		// 用于设置QR二维码参数
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别——这里选择最高H级别
		hints.put(EncodeHintType.ERROR_CORRECTION, level);
		/*
		 * 指定编码格式:注意这里utf-8一定要小写 这样就可以解决手机不能识别的问题，而且也能支持中文。
		 * 至于原因，查看了源代码后，发现使用“UTF-8”，会在文本编码前添加一段ECI(扩充解释Extended Channel Interpretation)
		 * 编码，就是这段编码导致手机不能解析。如果使用小写"utf-8"会使这个ECI判断失效而不影响内容编码方式。 至于详细的ECI解释，可以看《QRCode
		 * 编码解码标准》
		 */
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 0);
		// 写入字节矩阵；参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
		BitMatrix byteMatrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints); // 写入字节矩阵。
		// 对矩阵进行放大
		byteMatrix = MatrixToImageWriter.updateBit(byteMatrix, margin);
		// 返回字节矩阵
		return byteMatrix;
	}

	public static byte[] bitMatrix(String content, int width, int height, ErrorCorrectionLevel level, String formatName) throws WriterException, IOException {
		/*
		 * 参数image表示获得的BufferedImage； 参数format表示图片的格式，比如“gif”等；
		 * 参数out表示输出流，如果要转成Byte数组，则输出流为ByteArrayOutputStream即可；
		 */
		try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();){
			// 生成加密后数据内容的二维码图片
			BitMatrix byteMatrix = bitMatrix(content, width, height, level);
			// 写的byte输出流
			MatrixToImageWriter.writeToStream(byteMatrix, formatName, byteArray);
			// 返回图标字节内容 byte[];
			byte[] binaryData = byteArray.toByteArray();
			return binaryData;
		}
	}

	public static Result parse(byte[] bytes) throws ReaderException, IOException {
		// 将bytes作为输入流；
		return parse(new ByteArrayInputStream(bytes));
	}

	public static Result parse(File file) throws FileNotFoundException, ReaderException, IOException {
		// 将bytes作为输入流；
		return parse(new FileInputStream(file));
	}

	public static Result parse(InputStream in) throws ReaderException, IOException {
		// 将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
		BufferedImage image = ImageIO.read(in);
		return parse(image);
	}
	
	public static Result parse(BufferedImage image) throws ReaderException, IOException {
		if (image == null) {
			System.out.println("Could not decode image");
			return null;
		}
		// 初始化BinaryBitmap对象
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		/*
		 * 指定编码格式:注意这里utf-8一定要小写 这样就可以解决手机不能识别的问题，而且也能支持中文。
		 * 至于原因，查看了源代码后，发现使用“UTF-8”，会在文本编码前添加一段ECI(扩充解释Extended Channel Interpretation)
		 * 编码，就是这段编码导致手机不能解析。如果使用小写"utf-8"会使这个ECI判断失效而不影响内容编码方式。 至于详细的ECI解释，可以看《QRCode
		 * 编码解码标准》
		 */
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);

		// 使用Google 二维码对象解析二维码
		return new MultiFormatReader().decode(bitmap, hints);
	}

	public static Result parseWithLogo(byte[] bytes) throws ReaderException, IOException {
		// 将bytes作为输入流；
		return parseWithLogo(new ByteArrayInputStream(bytes));
	}
	
	public static Result parseWithLogo(InputStream in) throws ReaderException, IOException {
		// 将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
		BufferedImage image = ImageIO.read(in);
		return parse(image);
	}
	
	public static Result parseWithLogo(BufferedImage image) throws ReaderException, IOException {
		
		if (image == null) {
			System.out.println("Could not decode image");
			return null;
		}
		// 初始化BinaryBitmap对象
		BufferedImageLuminanceSource source = new BufferedImageWithLogoLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		// 使用Google 二维码对象解析二维码
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		/*
		 * 指定编码格式:注意这里utf-8一定要小写 这样就可以解决手机不能识别的问题，而且也能支持中文。
		 * 至于原因，查看了源代码后，发现使用“UTF-8”，会在文本编码前添加一段ECI(扩充解释Extended Channel Interpretation)
		 * 编码，就是这段编码导致手机不能解析。如果使用小写"utf-8"会使这个ECI判断失效而不影响内容编码方式。 至于详细的ECI解释，可以看《QRCode
		 * 编码解码标准》
		 */
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);

		return new MultiFormatReader().decode(bitmap, hints);
	}


}
