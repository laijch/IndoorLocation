package com.indoorloc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.indoorloc.ShopSignMatching.MatlabClient;
import com.indoorloc.model.ImageDivider;
import com.indoorloc.model.MyImageIO;
import com.indoorloc.model.Point;

public class RequestImage extends HttpServlet {
//	MatlabClient matlabClient = new MatlabClient("localhost", 6677);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		//把3张图片的路径传给Matlab服务器，获取类别
//		String[] imagePath = {"5", "2", "4"};
//		try {
//			matlabClient.shopSignClassification(imagePath);
//			Point[] shopPos = matlabClient.getShopPosition();
//			
//			for (int i = 0; i < shopPos.length; i++) {
//				System.out.println("ShopPos" + i + " : (" + shopPos[i].x + ", " + shopPos[i].y + ")");
//			}
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//		}

		//回传图片
		MyImageIO myImageIO = new MyImageIO();
		
		//项目部署后的根目录，如 D:\JavaServerProject\.metadata\.me_tcat\webapps\IndoorLocServer\
		String basepath = this.getServletContext().getRealPath("/");
		System.out.println("basepath: " + basepath);
		
		ImageDivider imageDivider = new ImageDivider(basepath + "image/shop_sign.jpg", new Point(10, 10), new Point(500, 500));
		myImageIO.myWrite(imageDivider.getShopSignImage(), basepath + "image/ShopSign/shop_sign_small.png");
		
		//返回web资源地址172.18.70.44
		response.getWriter().append("http://222.200.185.76:9302/IndoorLocServer/image/ShopSign/shop_sign_small.png");
    }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
    
    
}
