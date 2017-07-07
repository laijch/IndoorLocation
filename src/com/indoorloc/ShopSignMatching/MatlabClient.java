package com.indoorloc.ShopSignMatching;

import com.indoorloc.model.Point;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Matlab�ͻ��ˣ���Matlab�����������������Ʒ���
 * @author Mo Haoran
 */
public class MatlabClient {
	static double ProbGap = 0.3;
	static double CloseDistance = 300;
	
	static String hostIP;
	static int port;
	static String[] imagePaths;
	static String[] classlist = {"", "", ""};
	
	String[] last_classlist = {"", "", ""};
	
	static Socket clientSocket;
	
	private static ClassToPosMap classToPosMap;
	
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
	 * @param serverMsg ��ʽΪ: 16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3
	 *                       / 16:0.98@12:0.7#16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3 
	 *                       / 16:0.98#16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3 
	 *                       / -1
	 */
	public static void getMatlabServerMsgFromThread(String serverMsg) throws Exception {
		if (!serverMsg.equals("-1")) {    //�������������
			
//			String[] classProbStr = serverMsg.split("#");    //16:0.98@12:0.7@11:0.3 ��ʽ3��
//			
//			boolean[] reasonableTopOne = {false, false, false};    //�Ƿ�Ϊ�ɿ�Top1
//			String[] classTagSet = {"", "", ""};;
//			
//			for (int i = 0; i < classProbStr.length; i++) {    //��ÿ�����̵�ǰ3/2/1
//				String[] singleClassProbStr = classProbStr[i].split("@");    //11:0.3 ��ʽ3/2/1��
//				
//				if (singleClassProbStr.length == 1) {    //ֻ��1�������Ϊ�ɿ���
//					String[] singleClass_ProbStr = singleClassProbStr[0].split(":");    //11��0.3
//					reasonableTopOne[i] = true;
//					classTagSet[i] = singleClass_ProbStr[0];
//				}
//				else {
//					String[] singleClass_ProbStr1 = singleClassProbStr[0].split(":");    //11��0.3
//					String[] singleClass_ProbStr2 = singleClassProbStr[1].split(":");
//					
//					double prob1 = Double.parseDouble(singleClass_ProbStr1[1]);
//					double prob2 = Double.parseDouble(singleClass_ProbStr2[1]);
//					if (prob1 - prob2 > ProbGap) {    //Top1�ĸ��ʴ���Top2���ʵ�һ����ֵ
//						reasonableTopOne[i] = true;
//						classTagSet[i] = singleClass_ProbStr1[0];
//					}
//				}
//			}
//			
//			
//			int reasonableCount = 0;    //�ɿ�Top1����
//			for (int i = 0; i < reasonableTopOne.length; i++) {
//				if (reasonableTopOne[i])
//					reasonableCount++;
//			}
//			
//			
//			if (reasonableCount == 0) {    //0������ʧ��
//				for (int i = 0; i < classlist.length; i++) {
//					classlist[i] = "-1";
//				}
//			}
//			else if (reasonableCount == 1) {    //1�����Ҳ��ɿ�����2����top2/top3����˭����������Ϊ��ȷ
//				int reasonIndex = -1;    //�ɿ���top1
//				for (int i = 0; i < reasonableTopOne.length; i++) {
//					if (reasonableTopOne[i]) {
//						reasonIndex = i;
//						break;
//					}
//				}
//				
//				//��ȡ�ɿ�top1�ĵ���λ��
//				String[] reasonSingleClassProbStr = classProbStr[reasonIndex].split("@");
//				String[] reasonSingleClass_ProbStr = reasonSingleClassProbStr[0].split(":");
//				int intTop1 = Integer.parseInt(reasonSingleClass_ProbStr[0]);
//				String Top1Tag = (intTop1 - 1) + "";
//				Point reasonPos = classToPosMap.getClassToPosMap().get(Top1Tag);
//				
//				//����2�����ɿ�top1
//				for (int i = 0; i < reasonableTopOne.length; i++) {
//					if (i != reasonIndex) {
//						String[] singleClassProbStr = classProbStr[i].split("@");    //11:0.3 ��ʽ3/2��
//						int j;
//						for (j = 0; j < singleClassProbStr.length; j++) {
//							String[] unreasonSingleClass_ProbStr = singleClassProbStr[0].split(":");
//							int intTopk = Integer.parseInt(unreasonSingleClass_ProbStr[0]);
//							String TopkTag = (intTopk - 1) + "";
//							Point unreasonPos = classToPosMap.getClassToPosMap().get(TopkTag);
//							
//							//��ɿ������С��һ����ֵ����Ϊ��ȷ��label
//							if (reasonPos.isCloseEnough(unreasonPos, CloseDistance)) {
//								classlist[i] = TopkTag;
//								break;
//							}
//						}
//						
//						//Top3/2�ľ�����ɿ��㶼��Զ
//						if (j == singleClassProbStr.length) {
//							
//						}
//					}
//				}
//			}
//			else if (reasonableCount == 2) {    //2�����Ҳ��ɿ����Ǹ���top2/top3����˭����������Ϊ��ȷ
//				int unreasonIndex;    //���ɿ���top1
//				for (int i = 0; i < reasonableTopOne.length; i++) {
//					if (!reasonableTopOne[i]) {
//						unreasonIndex = i;
//						break;
//					}
//				}
//				
//				
//			}
//			else {    //3��������tagֱ�Ӹ�ֵ
//				for (int i = 0; i < classTagSet.length; i++) {
//					int intTag = Integer.parseInt(classTagSet[i]);
//					String classTag = (intTag - 1) + "";
//					classlist[i] = classTag;
//				}
//			}
			
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
	 * ����classlist
	 */
	public String[] getClasslist() {
		return last_classlist;
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
			
			last_classlist[i] = classlist[i];
			classlist[i] = "";
		}
		return shopPosSet;
	}
}
