package com.indoorloc.controller;

import java.io.IOException;

import javax.security.auth.x500.X500Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.indoorloc.model.Point;
import com.indoorloc.model.TrianglePosition;

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
		int ax = Integer.parseInt(request.getParameter("ax"));
		int ay = Integer.parseInt(request.getParameter("ay"));
		int bx = Integer.parseInt(request.getParameter("bx"));
		int by = Integer.parseInt(request.getParameter("by"));
		int cx = Integer.parseInt(request.getParameter("cx"));
		int cy = Integer.parseInt(request.getParameter("cy"));
		double apb = Double.parseDouble(request.getParameter("apb"));
		double bpc = Double.parseDouble(request.getParameter("bpc"));
		
		Point a = new Point(ax, ay);
		Point b = new Point(bx, by);
		Point c = new Point(cx, cy);
		TrianglePosition tp = new TrianglePosition(a, b, c, apb, bpc);
		/**格式："x|y" */
		String res = tp.getUserPositionStr();
		
		System.out.println(res);
		response.getWriter().append(res);
    }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
