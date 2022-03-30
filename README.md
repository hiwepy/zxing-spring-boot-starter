# zxing-spring-boot-starter

### 组件简介

> 基于 Google Zxing 的 Spring Boot Starter 实现

> ZXing（“zebra crossing”）是一个支持多种格式的条形码和二维码图形解析的开源Java类库，同时它也提供了其他语言的接口。
> 解码时支持格式有：UPC-A、UPC-E、EAN-8、EAN-13、Code 39、Code 93、Code 128、ITF、Codabar、MSI、RSS-14、QR Code、Data Matrix、Aztec and PDF-417。
> 编码时支持的格式有：UPC-A、EAN-8、EAN-13、Code 39、Code 128、ITF、Codabar、Plessey、MSI、QR Code、PDF-417、Aztec、Data Matrix。

### 下列平台均有对应版本程序：

1. .Net 2.0、3.5及4.0
1. Silverlight 4、5
1. Windows Phone 7.0、7.1和8.0
1. Windows CE
1. Windows RT Class Library and Runtime Components
1. Portable Class Library
1. Unity3D
1. Xamarin.Android

### 使用说明

##### 1、Spring Boot 项目添加 Maven 依赖

``` xml
<dependency>
	<groupId>com.github.hiwepy</groupId>
	<artifactId>zxing-spring-boot-starter</artifactId>
	<version>${project.version}</version>
</dependency>
```

##### 2、在`application.yml`文件中增加如下配置

```yaml
```

##### 3、使用示例


条码示例：

```java

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

```

二维码示例：

```java

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
				+ "\nBlog [ https://jeebiz.cn ]"
				+ "\nEMail [ hiwepy@163.com ]";
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
```

## Jeebiz 技术社区

Jeebiz 技术社区 **微信公共号**、**小程序**，欢迎关注反馈意见和一起交流，关注公众号回复「Jeebiz」拉你入群。

|公共号|小程序|
|---|---|
| ![](https://raw.githubusercontent.com/hiwepy/static/main/images/qrcode_for_gh_1d965ea2dfd1_344.jpg)| ![](https://raw.githubusercontent.com/hiwepy/static/main/images/gh_09d7d00da63e_344.jpg)|


