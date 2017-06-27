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

import com.indoorloc.ShopSignMatching.MatlabClient;
import com.indoorloc.model.Point;

public class UploadImage extends HttpServlet {
	
	MatlabClient matlabClient = new MatlabClient("172.19.128.222", 6677);
	String[] imagePath = {"5", "2", "4"};
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		saveImage(request, response);
		//把3张图片的路径传给Matlab服务器，获取类别
		try {
			matlabClient.shopSignClassification(imagePath);
			Point[] shopPos = matlabClient.getShopPosition();
			
			for (int i = 0; i < shopPos.length; i++) {
				System.out.println("ShopPos" + i + " : (" + shopPos[i].x + ", " + shopPos[i].y + ")");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
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
				String path = genericPath(filename, storeDirectory);
				
				// 处理文件的上传
				try {
					fileItem.write(new File(storeDirectory + path, filename));
					
//					String filePath = "/image" + path + "/" + filename;
					// 把图片路径赋值给imagePath
					imagePath[i] = "http://172.18.70.44:8080/IndoorLocServer/image" + path + "/" + filename;
					System.out.println(i+": success, path="+imagePath[i]);
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
