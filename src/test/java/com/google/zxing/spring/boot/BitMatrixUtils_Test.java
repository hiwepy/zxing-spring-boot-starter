package com.google.zxing.spring.boot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.google.zxing.spring.boot.utils.BitMatrixUtils;
import com.google.zxing.spring.boot.utils.QRCodeUtils;

import junit.framework.TestCase;

public class BitMatrixUtils_Test extends TestCase {
	
	
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

		QRCodeUtils.toQrcode(contents2, width2, height2, new FileOutputStream(imgPath2));
		System.out.println("finished zxing encode.");

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
