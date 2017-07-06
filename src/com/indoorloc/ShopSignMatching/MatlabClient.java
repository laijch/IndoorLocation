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
	
	static Socket clientSocket;
	
	private ClassToPosMap classToPosMap;
	
	public MatlabClient(String _hostIP, int _port) {
		hostIP = _hostIP;
		port = _port;
		classToPosMap = new ClassToPosMap();
	}

	/**
	 * ���̷������
	 * @param _imagePaths  ͼƬ·����
	 */
	public void shopSignClassification(String[] _imagePaths) throws Exception {
		imagePaths = _imagePaths;
		String allImagePaths = "";    //����ͼƬ��url�ϳ�һ���ַ���
		for (int i = 0; i < imagePaths.length; i++) {
			allImagePaths += imagePaths[i];
			if (i != imagePaths.length - 1)
				allImagePaths += "|";
		}

		System.out.println("hostIP | port: " + hostIP + " | " + port);
		clientSocket = new Socket(hostIP, port);    //������һ��socket
		interactWithMatlabServer(allImagePaths);
	}
	
	/**
	 * ����socket����Matlab�����������з���
	 * @param AllImagePaths  ͼ�񼯺�·��
	 */
	private static void interactWithMatlabServer(String AllImagePaths) throws Exception {
		//���Ӻ󣬸�Matlab��������������
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		outToServer.println(AllImagePaths);
		
		//��Matlab�����������߳�
		ClientListenThread clientListenThread = new ClientListenThread(clientSocket);
		clientListenThread.start();
	}
	
	/**
	 * ���̻߳��Matlab�������󣬴������߳�
	 * @param serverMsg ��ʽΪ��16:18:54 / -1
	 */
	public static void getMatlabServerMsgFromThread(String serverMsg) throws Exception {
		if (!serverMsg.equals("-1")) {    //�������������
			String[] classTagSet = serverMsg.split(":");

			for (int i = 0; i < classTagSet.length; i++) {
				int intTag = Integer.parseInt(classTagSet[i]);
				String classTag = (intTag - 1) + "";
				classlist[i] = classTag;
			}
		}
		else {    //�ϴ�ͼƬ�޷��ҵ�ƥ������������Ϊ-1�����ͻ�����ʾ
			for (int i = 0; i < classlist.length; i++) {
				classlist[i] = "-1";
			}
		}
		
		clientSocket.close();
	}
	
	/**
	 * ͨ������Լ�ӳ�����3�����̵ĵ���λ��
	 */
	public Point[] getShopPosition() throws InterruptedException {
		//�ȵ�3������������ȡ�ſɷ���
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
				shopPosSet[i] = new Point(-1, -1);    //�޷�ƥ��ʱ�����ص�����(-1, -1)
			
			classlist[i] = "";
		}
		return shopPosSet;
	}
}
