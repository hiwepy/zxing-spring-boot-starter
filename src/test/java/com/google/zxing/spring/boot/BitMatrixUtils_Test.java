package com.google.zxing.spring.boot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.google.zxing.spring.boot.utils.BitMatrixUtils;

import junit.framework.TestCase;
import net.coobird.thumbnailator.Thumbnails;

public class BitMatrixUtils_Test extends TestCase {
	
	ZxingQrCodeTemplate qrCodeTemplate = new ZxingQrCodeTemplate();
	
	public void testParse() throws Exception{
		String imgPath = "D:/test.png"; 

		try { 
			String encryptedText = BitMatrixUtils.parse(new File(imgPath)).getText();
			
			System.out.println("解压后字符串:\t" + encryptedText);
			System.out.println("解压后长度:\t" + encryptedText.length());
			
		} catch (Exception e) { 
		    System.out.println(e.toString()); 
		} 
		
		// 二维码
		String imgPath2 = "D:\\zxing.png";
		String contents2 = "Hello Gem, welcome to Zxing!"
				+ "\nBlog [ http://jeeplus.iteye.com ]"
				+ "\nEMail [ jeeplus@163.com ]";
		int width2 = 300, height2 = 300;

		qrCodeTemplate.qrcode(contents2, width2, height2, new FileOutputStream(imgPath2));
		System.out.println("finished zxing encode.");

		Thumbnails.of("images/a380_1280x1024.jpg")  
	    .scale(0.25f)  
	    .toFile("c:/a380_25%.jpg"); 
		
		
		String decodeContent2 = BitMatrixUtils.parse(new File(imgPath2)).getText();
		System.out.println("解码内容如下：" + decodeContent2);
		System.out.println("finished zxing decode.");
		
	}
	
	public void testParseWithLogo() throws Exception{
		String imgPath = "D:/test2.png"; 

		try { 
			String encryptedText = BitMatrixUtils.parseWithLogo(new FileInputStream(imgPath)).getText();
			
			System.out.println("解压后字符串:\t" + encryptedText);
			System.out.println("解压后长度:\t" + encryptedText.length());
			
		} catch (Exception e) { 
		    System.out.println(e.toString()); 
		} 
		
	}

}
