package com.indoorloc.ShopSignMatching;

import com.indoorloc.model.Point;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Matlab�ͻ��ˣ���Matlab�����������������Ʒ���
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
	 * ���̷������
	 * @param _imagePaths  ͼƬ·��
	 */
	public void shopSignClassification(String[] _imagePaths) throws Exception {
		imagePaths = _imagePaths;
		interactWithMatlabServer(imagePaths[0]);
	}
	
	/**
	 * ����socket����Matlab�����������з���
	 * @param imagePath  ����ͼ��·��
	 */
	private static void interactWithMatlabServer(String imagePath) throws Exception {
		Socket clientSocket = new Socket(hostIP, port);
		
		//���Ӻ󣬸���������������
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		outToServer.println(imagePath);
		
		//�ӷ����������߳�
		ClientListenThread clientListenThread = new ClientListenThread(clientSocket);
		clientListenThread.start();
		
	}
	
	/**
	 * ���̻߳�÷���󣬴������߳�
	 * @param serverMsg
	 */
	public static void setClassTag(String classTag) throws Exception {
		for (int i = 0; i < classlist.length; i++) {
			if (classlist[i] == "") {
				classlist[i] = classTag;
				if (i + 1 < imagePaths.length)
					interactWithMatlabServer(imagePaths[i + 1]);
				
				break;
			}
		}
		
//		System.out.println("Main Thread Print!");
	}
	
	/**
	 * ͨ������Լ�ӳ�����3�����̵ĵ���λ��
	 */
	public Point[] getShopPosition() throws InterruptedException {
		while (classlist[2] == "") {
			Thread.sleep(300);
		}
		
		Point[] shopPosSet = new Point[3];
		for (int i = 0; i < 3; i++) {
			shopPosSet[i] = classToPosMap.getClassToPosMap().get(classlist[i]);
			classlist[i] = "";
		}
		return shopPosSet;
	}
}
