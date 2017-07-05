package com.indoorloc.controller;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore.LoadStoreParameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.indoorloc.ShopSignMatching.MatlabClient;
import com.indoorloc.model.Point;
import com.indoorloc.model.TrianglePosition;

public class UploadImage extends HttpServlet {
	
	private MatlabClient matlabClient = new MatlabClient("localhost", 6677);
	private String[] imagePath = {"5", "2", "4"};
	private Point[] shopPos;
	private double angle1;  // 弧度角
	private double angle2;  // 弧度角
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		// 保存图片和参数
		savefileItem(request, response);
		
		//把3张图片的路径传给Matlab服务器，获取类别
		try {
			matlabClient.shopSignClassification(imagePath);
			shopPos = matlabClient.getShopPosition();
			
			for (int i = 0; i < shopPos.length; i++) {
				System.out.println("ShopPos" + i + " : (" + shopPos[i].x + ", " + shopPos[i].y + ")");
			}
		} 
		catch (Exception e) {
			System.out.println("获取位置失败");
			response.getWriter().append("get position fail");
			e.printStackTrace();
		}
		
		String res = "";
		// 判断图片能否匹配成功
		Point wrong = new Point(-1, -1);
		if (shopPos[0].equal(wrong) && shopPos[1].equal(wrong) && shopPos[2].equal(wrong)) {
			res = "match failure.";
			System.out.println(res);
		} else {
			// 三角定位计算用户地址
			TrianglePosition tp = new TrianglePosition(shopPos[0], shopPos[1], shopPos[2], angle1, angle2);
			/**格式："x|y" */
			res = tp.getUserPositionStr();
			System.out.println("User Position: " + res);
			/**测试：返回一定范围的随机位置(20,20)~(1300,2900)*/
			Random random = new Random();
			int x = random.nextInt(1300)%1281+20;
			int y = random.nextInt(2900)%2881+20;
			res = x+"|"+y;
			System.out.println("User Position FOR TEST: " + res);
		}
		
		response.getWriter().append(res);
    }
	
	//保存图片和参数
	private void savefileItem(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		DiskFileItemFactory dff = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(dff);
		try {
			System.out.println("--------Start saving--------");
			List<FileItem> items = sfu.parseRequest(request);
			
			for(int i = 0; i < items.size(); i++) {
				// 获取上传字段
				FileItem fileItem = items.get(i);
				
				if (fileItem.isFormField()) {
					// 普通文本处理（参数）
					String paramName = fileItem.getFieldName();
					String paramValue = fileItem.getString();
					System.out.println(paramName + ":" + paramValue);
					// 参数为角度时，转换为弧度值并保存
					if (paramName.equals("angle1")) {
						double angle = Double.parseDouble(paramValue);
						angle1 = Math.toRadians(angle);
						System.out.println(paramName + "(radian value):" + angle1);
					} else if (paramName.equals("angle2")) {
						double angle = Double.parseDouble(paramValue);
						angle2 = Math.toRadians(angle);
						System.out.println(paramName + "(radian value):" + angle2);
					}
				} else {
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
						imagePath[i] = "http://localhost:8080/IndoorLocServer/image" + path + "/" + filename;
						System.out.println(i+": success, path="+imagePath[i]);
						System.out.println(filename);
					} catch (Exception e) {
						System.out.println("Image upload failure1.");
						response.getWriter().append("Image upload failure1.");
					}
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
