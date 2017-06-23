package com.indoorloc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadSensorMsg extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String magnetic = request.getParameter("magnetic");
		String compass = request.getParameter("compass");
		String gyroscope = request.getParameter("gyroscope");
		String acceleration = request.getParameter("acceleration");
        System.out.println("magnetic：" + magnetic);
        System.out.println("compass：" + compass);
        System.out.println("gyroscope：" + gyroscope);
        System.out.println("acceleration：" + acceleration);
		
		double x = Math.random() * 10.0;
		double y = Math.random() * 10.0;
		String res = "(" + x + ", " + y + ")";
		response.getWriter().append(res);
    }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
    
    
}