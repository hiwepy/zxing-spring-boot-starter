package com.google.zxing.spring.boot;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import junit.framework.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarCodeTest extends TestCase {
	
	ZxingBarCodeTemplate barCodeTemplate = new ZxingBarCodeTemplate();
		
	// 条形码
	String imgPath = "D:\\zxing_EAN13.png";

	public void test1Encode() {
		String contents = "6923450657713";
		int width = 105, height = 50;
		
		barCodeTemplate.encode(contents, width, height, imgPath);
		System.out.println("finished zxing EAN-13 encode.");
	}

	public void test2Decode() {

		String decodeContent = barCodeTemplate.decode(imgPath);
		System.out.println("解码内容如下：" + decodeContent);
		System.out.println("finished zxing EAN-13 decode.");
		
	}
	
}
