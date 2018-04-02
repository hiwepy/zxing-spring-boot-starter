/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.spring.boot.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.common.BitMatrix;

/**
 * Writes a {@link BitMatrix} to {@link BufferedImage}, file or stream. Provided
 * here instead of core since it depends on Java SE libraries.
 * 
 * @author Sean Owen
 */
public final class MatrixToImageWriter {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private MatrixToImageWriter() {
	}

	/**
	 * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
	 * as white, and "true" bits are rendered as black.
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		//第一步：将zxing生成的二维码图标矩阵绘制到BufferedImage，边距这时较大
		int width  = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	
	
	public static BufferedImage toBufferedImage(BitMatrix matrix,int size) {
        //因为二维码生成时，白边无法控制，去掉原有的白边，再添加自定义白边后，二维码大小与size大小就存在差异了，为了让新生成的二维码大小还是size大小，根据size重新生成图片
        BufferedImage image =  MatrixToImageWriter.toBufferedImage(matrix);
        //根据size放大、缩小生成的二维码
        return zoomInImage(image,size,size);
	}
	
	
	/**
	 * 
	 * @description: 因为二维码边框设置那里不起作用，不管设置多少，都会生成白边，所以根据网上的例子进行修改，自定义控制白边宽度， 该方法生成自定义白边框后的bitMatrix；
	 * @author : vindell
	 * @date 下午4:02:57 2014-10-25 
	 * @param matrix
	 * @param margin
	 * @return
	 * @return  BitMatrix 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static BitMatrix updateBit(BitMatrix matrix, int margin) {
		int tempM = margin * 2;
		int[] rec = matrix.getEnclosingRectangle(); // 获取二维码图案的属性
		int resWidth = rec[2] + tempM;
		int resHeight = rec[3] + tempM;
		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
		resMatrix.clear();
		for (int i = margin; i < resWidth - margin; i++) { // 循环，将二维码图案绘制到新的bitMatrix中
			for (int j = margin; j < resHeight - margin; j++) {
				if (matrix.get(rec[0] + (i - margin), rec[1] + ( j - margin ))) {
					resMatrix.set(i, j);
				}
			}
		}
		return resMatrix;
	}

	/**
     * 图片放大缩小
     */
    public static BufferedImage  zoomInImage(BufferedImage  originalImage, int width, int height){
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0,0,width,height,null);
        g.dispose();
        return newImage;
    }

	/**
	 * Writes a {@link BitMatrix} to a file.
	 * 
	 * @see #toBufferedImage(BitMatrix)
	 */
	public static void writeToFile(BitMatrix matrix, String format, File file)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		ImageIO.write(image, format, file);
	}

	/**
	 * Writes a {@link BitMatrix} to a stream.
	 * 
	 * @see #toBufferedImage(BitMatrix)
	 */
	public static void writeToStream(BitMatrix matrix, String format,
			OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		ImageIO.write(image, format, stream);
	}

}
