package com.indoorloc.controller;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore.LoadStoreParameter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadImage extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		saveImage(request, response);
		
		double x = Math.random() * 10.0;
		double y = Math.random() * 10.0;
		String res = "(" + x + ", " + y + ")";
		response.getWriter().append(res);
    }
	
	//保存图片
	private void saveImage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		DiskFileItemFactory dff = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(dff);
		try {
			List<FileItem> items = sfu.parseRequest(request);
			List<String> paths;
			
			for(int i = 0; i < items.size(); i++) {
				// 获取上传字段
				FileItem fileItem = items.get(i);
				
				// 文件名
				String filename = fileItem.getName();

				// 生成存储路径
				String storeDirectory = getServletContext().getRealPath("/image");
				File file = new File(storeDirectory);
				if (!file.exists()) {
					file.mkdir();
				}
				String pathi = genericPath(filename, storeDirectory);
				//paths.add(pathi);
				
				// 处理文件的上传
				try {
					fileItem.write(new File(storeDirectory + pathi, filename));
					
					String filePath = "/image" + pathi + "/" + filename;
					System.out.println(i+": success, path="+pathi);
					response.getWriter().append("success");
				} catch (Exception e) {
					System.out.println("Image upload failure1.");
					response.getWriter().append("Image upload failure1.");
				}
			}
		} 
		catch (Exception e) {
			System.out.println("Image upload failure2.");
			response.getWriter().append("Image upload failure2.");
		}
	}
	//计算文件的存放目录
	private String genericPath(String filename, String storeDirectory) {
		int hashCode = filename.hashCode();
		int dir1 = hashCode&0xf;
		int dir2 = (hashCode&0xf0)>>4;
		
		String dir = "/"+dir1+"/"+dir2;
		
		File file = new File(storeDirectory,dir);
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
