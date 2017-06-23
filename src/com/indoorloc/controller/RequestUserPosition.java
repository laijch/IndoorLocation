package com.indoorloc.controller;

import java.io.IOException;

import javax.security.auth.x500.X500Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestUserPosition extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		/*
		 * (ax,ay),(bx,by),(cx,cy)分别为用户原地旋转依次拍到的店铺a、b、c的坐标
		 * p点为用户位置，apb、bpc均代表用户在对应商店间的旋转角<弧度制>
		 * 
		 * 例如：222.200.185.76:9302/IndoorLocServer/RequestUserPosition?ax=0&ay=1&bx=0&by=0&cx=1&cy=0&apb=0.785398&bpc=0.785398
		 */
		double ax = Double.parseDouble(request.getParameter("ax"));
		double ay = Double.parseDouble(request.getParameter("ay"));
		double bx = Double.parseDouble(request.getParameter("bx"));
		double by = Double.parseDouble(request.getParameter("by"));
		double cx = Double.parseDouble(request.getParameter("cx"));
		double cy = Double.parseDouble(request.getParameter("cy"));
		double apb = Double.parseDouble(request.getParameter("apb"));
		double bpc = Double.parseDouble(request.getParameter("bpc"));
		
		double bc = Math.sqrt(Math.pow(cx-bx,2)+Math.pow(cy-by,2));
		double ab = Math.sqrt(Math.pow(ax-bx,2)+Math.pow(ay-by,2));
		// 原theta值<弧度制>推导公式有误，以下是正确公式
		double theta = Math.acos(((cx-bx)*(ax-bx)+(cy-by)*(ay-by))/(bc*ab));
		// 中间值计算
		double cot_bpc = Math.cos(bpc)/Math.sin(bpc);
		double sin_apb_theta = Math.sin(apb+theta);
		double cos_apb_theta = Math.cos(apb+theta);
		double sin_apb = Math.sin(apb);
		double num1 = bc*ab*(sin_apb_theta*cot_bpc+cos_apb_theta);
		double denominator = Math.pow(ab*sin_apb_theta-bc*sin_apb, 2)
				+Math.pow(ab*cos_apb_theta+bc*sin_apb*cot_bpc, 2);
		
		double x0 = num1*(bc*sin_apb*cot_bpc+ab*cos_apb_theta)/denominator;
		double y0 = num1*(ab*sin_apb_theta-bc*sin_apb)/denominator;
		
		//用户坐标
		double x = x0*(cx-bx)/bc-y0*(cy-by)/bc+bx;
		double y = x0*(cy-by)/bc+y0*(cx-bx)/bc+by;
		
		System.out.println("theta：" + theta);
		System.out.println("bc：" + bc);
		System.out.println("ab：" + ab);
		System.out.println("x0：" + x0);
		System.out.println("y0：" + y0);
		System.out.println("x：" + x);
		System.out.println("y：" + y);
		
		String res = "(" + x + ", " + y + ")";
		response.getWriter().append(res);
    }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
