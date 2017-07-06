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
	
	static Socket clientSocket;
	
	private ClassToPosMap classToPosMap;
	
	public MatlabClient(String _hostIP, int _port) {
		hostIP = _hostIP;
		port = _port;
		classToPosMap = new ClassToPosMap();
	}

	/**
	 * 店铺分类入口
	 * @param _imagePaths  图片路径集
	 */
	public void shopSignClassification(String[] _imagePaths) throws Exception {
		imagePaths = _imagePaths;
		String allImagePaths = "";    //三张图片的url合成一个字符串
		for (int i = 0; i < imagePaths.length; i++) {
			allImagePaths += imagePaths[i];
			if (i != imagePaths.length - 1)
				allImagePaths += "|";
		}

		System.out.println("hostIP | port: " + hostIP + " | " + port);
		clientSocket = new Socket(hostIP, port);    //仅创建一个socket
		interactWithMatlabServer(allImagePaths);
	}
	
	/**
	 * 单个socket连接Matlab服务器，进行分类
	 * @param AllImagePaths  图像集合路径
	 */
	private static void interactWithMatlabServer(String AllImagePaths) throws Exception {
		//连接后，给Matlab服务器发送数据
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		outToServer.println(AllImagePaths);
		
		//从Matlab服务器读入线程
		ClientListenThread clientListenThread = new ClientListenThread(clientSocket);
		clientListenThread.start();
	}
	
	/**
	 * 子线程获得Matlab服务器后，传回主线程
	 * @param serverMsg 格式为：16:18:54 / -1
	 */
	public static void getMatlabServerMsgFromThread(String serverMsg) throws Exception {
		if (!serverMsg.equals("-1")) {    //返回真正的类别
			String[] classTagSet = serverMsg.split(":");

			for (int i = 0; i < classTagSet.length; i++) {
				int intTag = Integer.parseInt(classTagSet[i]);
				String classTag = (intTag - 1) + "";
				classlist[i] = classTag;
			}
		}
		else {    //上传图片无法找到匹配类别，设置类别为-1，给客户端提示
			for (int i = 0; i < classlist.length; i++) {
				classlist[i] = "-1";
			}
		}
		
		clientSocket.close();
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
			if (!classlist[i].equals("-1")) {
				System.out.println("Class: " + classlist[i]);
				shopPosSet[i] = classToPosMap.getClassToPosMap().get(classlist[i]);
			}
			else
				shopPosSet[i] = new Point(-1, -1);    //无法匹配时，返回点坐标(-1, -1)
			
			classlist[i] = "";
		}
		return shopPosSet;
	}
}
