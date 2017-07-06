package com.indoorloc.ShopSignMatching;

import com.indoorloc.model.Point;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Matlab客户端，与Matlab服务器交互进行招牌分类
 * @author Mo Haoran
 */
public class MatlabClient {
	static String hostIP;
	static int port;
	static String[] imagePaths;
	static String[] classlist = {"", "", ""};
	
	private ClassToPosMap classToPosMap;
	
	public MatlabClient(String _hostIP, int _port) {
		hostIP = _hostIP;
		port = _port;
		classToPosMap = new ClassToPosMap();
	}

	/**
	 * 店铺分类入口
	 * @param _imagePaths  图片路径
	 */
	public void shopSignClassification(String[] _imagePaths) throws Exception {
		imagePaths = _imagePaths;
		interactWithMatlabServer(imagePaths[0]);
	}
	
	/**
	 * 单个socket连接Matlab服务器，进行分类
	 * @param imagePath  单张图像路径
	 */
	private static void interactWithMatlabServer(String imagePath) throws Exception {
		System.out.println("hostIP | port: " + hostIP + " | " + port);
		
		Socket clientSocket = new Socket(hostIP, port);
		
		//连接后，给服务器发送数据
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		outToServer.println(imagePath);
		
		//从服务器读入线程
		ClientListenThread clientListenThread = new ClientListenThread(clientSocket);
		clientListenThread.start();
		
	}
	
	/**
	 * 子线程获得分类后，传回主线程
	 * @param serverMsg
	 */
	public static void setClassTag(String classTag) throws Exception {
		if (!classTag.equals("-1")) {    //返回真正的类别
			for (int i = 0; i < classlist.length; i++) {
				if (classlist[i] == "") {
					int intTag = Integer.parseInt(classTag);
					classTag = (intTag - 1)+"";
					classlist[i] = classTag;
					if (i + 1 < imagePaths.length)
						interactWithMatlabServer(imagePaths[i + 1]);
					
					break;
				}
			}
		}
		else {    //上传图片无法找到匹配类别，设置类别为-1，给客户端提示
			for (int i = 0; i < classlist.length; i++) {
				classlist[i] = "-1";
			}
		}
		
//		System.out.println("Main Thread Print!");
	}
	
	/**
	 * 通过类别以及映射表返回3个商铺的地理位置
	 */
	public Point[] getShopPosition() throws InterruptedException {
		//等到3个店铺类别均获取才可返回
		while (classlist[2] == "") {
			Thread.sleep(300);
		}
		
		Point[] shopPosSet = new Point[3];
		for (int i = 0; i < 3; i++) {
			if (!classlist[i].equals("-1"))
				shopPosSet[i] = classToPosMap.getClassToPosMap().get(classlist[i]);
			else
				shopPosSet[i] = new Point(-1, -1);    //无法匹配时，返回点坐标(-1, -1)
			
			classlist[i] = "";
		}
		return shopPosSet;
	}
}
