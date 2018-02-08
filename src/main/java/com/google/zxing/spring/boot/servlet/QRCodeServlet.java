package com.google.zxing.spring.boot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.spring.boot.utils.BitMatrixUtils;

/**
 * 
 * @className	： QRCodeServlet
 * @description	：  二维码生成Servlet
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2018年2月3日 下午9:52:52
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class QRCodeServlet extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//二维码内容
		String content = request.getParameter("content");
		//二维码图片宽度
		String width   = request.getParameter("w");
		//二维码图片高度
		String height  = request.getParameter("h");
		
		if(content==null||content.equals("")){
			response.setContentType("text/plain;charset=UTF-8");
			response.getOutputStream().write("二维码内容不可为空!".getBytes("utf-8"));
			response.getOutputStream().close();
		}
		
		int imgWidth  = 110;
		int imgHeight = 110;
		
		if(width!=null&&!width.equals("")){
			try {
				imgWidth = Integer.parseInt(width);
			} catch (Exception e) {}
		}
		if(height!=null&&!height.equals("")){
			try {
				imgHeight = Integer.parseInt(height);
			} catch (Exception e) {}
		}
		try {
			
			BitMatrix byteMatrix = BitMatrixUtils.toMatrix(new String(content.getBytes(BitMatrixUtils.CHARSET),"ISO-8859-1"), imgWidth, imgHeight);
			response.setContentType("image/png");
			MatrixToImageWriter.writeToStream(byteMatrix , "png", response.getOutputStream());
			
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}
	
}
