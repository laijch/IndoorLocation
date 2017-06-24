package com.indoorloc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.indoorloc.model.ImageDivitor;
import com.indoorloc.model.MyImageIO;
import com.indoorloc.model.Point;

public class RequestImage extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		MyImageIO myImageIO = new MyImageIO();
		
		//��Ŀ�����ĸ�Ŀ¼���� D:\JavaServerProject\.metadata\.me_tcat\webapps\IndoorLocServer\
		String basepath = this.getServletContext().getRealPath("/");
		System.out.println("basepath: " + basepath);
		
		ImageDivitor imageDivitor = new ImageDivitor(basepath + "image/shop_sign.jpg", new Point(10, 10), new Point(500, 500));
		myImageIO.myWrite(imageDivitor.getShopSignImage(), basepath + "image/ShopSign/shop_sign_small.png");
		
		//����web��Դ��ַ
		response.getWriter().append("http://172.18.70.44:8080/IndoorLocServer/image/ShopSign/shop_sign_small.png");
    }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
    
    
}
