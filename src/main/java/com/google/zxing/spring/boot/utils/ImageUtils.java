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
package com.google.zxing.spring.boot.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

/**
 * TODO
 * @author 		： <a href="https://github.com/vindell">wandl</a>
 */
public class ImageUtils {

	public static BufferedImage buffered(Image source) throws IOException {
		if(source instanceof BufferedImage) {
			return (BufferedImage) source;
		}
		BufferedImage redraw = new BufferedImage(source.getWidth(null), source.getWidth(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = redraw.getGraphics();
		g.drawImage(source, 0, 0, null); 
		g.dispose();
		return redraw;
	}
	
	public static BufferedImage scale(Image source, int height, int width) throws IOException {
		return scale(buffered(source), height, width);
	}
		
	public static BufferedImage scale(BufferedImage source, int height, int width) throws IOException {
		
		/**
		double ratioW = (new Integer(width)).doubleValue() / source.getWidth();
		double ratioH = (new Integer(height)).doubleValue() / source.getHeight();
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratioW, ratioH), null);
		Image scaleImage = op.filter(source, null);
		*/
		
		// 图片尺寸缩放
		Image scaleImage = Thumbnails.of(source).size(width, height).asBufferedImage();
		// 自动补白
		return filler(scaleImage, height, width);
	}
	
	public static BufferedImage filler(BufferedImage source, int height, int width) throws IOException {
		return filler((Image) source, height, width);
	}
	
	public static BufferedImage filler(Image source, int height, int width) throws IOException {
		// 补白
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphic = image.createGraphics();
		graphic.setColor(Color.white);
		graphic.fillRect(0, 0, width, height);
		if (width == source.getWidth(null)) {
			graphic.drawImage(source, 0, (height - source.getHeight(null)) / 2, source.getWidth(null),
					source.getHeight(null), Color.white, null);
		} else {
			graphic.drawImage(source, (width - source.getWidth(null)) / 2, 0, source.getWidth(null),
					source.getHeight(null), Color.white, null);
		}
		graphic.dispose();
		// 返回补白后的图片对象
		return image;
	}
	
}
