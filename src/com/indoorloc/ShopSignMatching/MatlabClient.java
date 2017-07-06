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
		System.out.println("hostIP | port: " + hostIP + " | " + port);
		
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
		if (!classTag.equals("-1")) {    //�������������
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
		else {    //�ϴ�ͼƬ�޷��ҵ�ƥ������������Ϊ-1�����ͻ�����ʾ
			for (int i = 0; i < classlist.length; i++) {
				classlist[i] = "-1";
			}
		}
		
//		System.out.println("Main Thread Print!");
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
			if (!classlist[i].equals("-1"))
				shopPosSet[i] = classToPosMap.getClassToPosMap().get(classlist[i]);
			else
				shopPosSet[i] = new Point(-1, -1);    //�޷�ƥ��ʱ�����ص�����(-1, -1)
			
			classlist[i] = "";
		}
		return shopPosSet;
	}
}
