package com.indoorloc.ShopSignMatching;

import com.indoorloc.model.Point;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Matlab客户端，与Matlab服务器交互进行招牌分类
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
	 * @param serverMsg 格式为: 16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3
	 *                       / 16:0.98@12:0.7#16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3 
	 *                       / 16:0.98#16:0.98@12:0.7@11:0.3#16:0.98@12:0.7@11:0.3 
	 *                       / -1
	 */
	public static void getMatlabServerMsgFromThread(String serverMsg) throws Exception {
		if (!serverMsg.equals("-1")) {    //返回真正的类别
			
//			String[] classProbStr = serverMsg.split("#");    //16:0.98@12:0.7@11:0.3 格式3个
//			
//			boolean[] reasonableTopOne = {false, false, false};    //是否为可靠Top1
//			String[] classTagSet = {"", "", ""};;
//			
//			for (int i = 0; i < classProbStr.length; i++) {    //对每个店铺的前3/2/1
//				String[] singleClassProbStr = classProbStr[i].split("@");    //11:0.3 格式3/2/1个
//				
//				if (singleClassProbStr.length == 1) {    //只有1个类别，视为可靠类
//					String[] singleClass_ProbStr = singleClassProbStr[0].split(":");    //11和0.3
//					reasonableTopOne[i] = true;
//					classTagSet[i] = singleClass_ProbStr[0];
//				}
//				else {
//					String[] singleClass_ProbStr1 = singleClassProbStr[0].split(":");    //11和0.3
//					String[] singleClass_ProbStr2 = singleClassProbStr[1].split(":");
//					
//					double prob1 = Double.parseDouble(singleClass_ProbStr1[1]);
//					double prob2 = Double.parseDouble(singleClass_ProbStr2[1]);
//					if (prob1 - prob2 > ProbGap) {    //Top1的概率大于Top2概率的一定阈值
//						reasonableTopOne[i] = true;
//						classTagSet[i] = singleClass_ProbStr1[0];
//					}
//				}
//			}
//			
//			
//			int reasonableCount = 0;    //可靠Top1个数
//			for (int i = 0; i < reasonableTopOne.length; i++) {
//				if (reasonableTopOne[i])
//					reasonableCount++;
//			}
//			
//			
//			if (reasonableCount == 0) {    //0，分类失败
//				for (int i = 0; i < classlist.length; i++) {
//					classlist[i] = "-1";
//				}
//			}
//			else if (reasonableCount == 1) {    //1个，找不可靠的那2个的top2/top3，看谁靠近，则视为正确
//				int reasonIndex = -1;    //可靠的top1
//				for (int i = 0; i < reasonableTopOne.length; i++) {
//					if (reasonableTopOne[i]) {
//						reasonIndex = i;
//						break;
//					}
//				}
//				
//				//获取可靠top1的地理位置
//				String[] reasonSingleClassProbStr = classProbStr[reasonIndex].split("@");
//				String[] reasonSingleClass_ProbStr = reasonSingleClassProbStr[0].split(":");
//				int intTop1 = Integer.parseInt(reasonSingleClass_ProbStr[0]);
//				String Top1Tag = (intTop1 - 1) + "";
//				Point reasonPos = classToPosMap.getClassToPosMap().get(Top1Tag);
//				
//				//对于2个不可靠top1
//				for (int i = 0; i < reasonableTopOne.length; i++) {
//					if (i != reasonIndex) {
//						String[] singleClassProbStr = classProbStr[i].split("@");    //11:0.3 格式3/2个
//						int j;
//						for (j = 0; j < singleClassProbStr.length; j++) {
//							String[] unreasonSingleClass_ProbStr = singleClassProbStr[0].split(":");
//							int intTopk = Integer.parseInt(unreasonSingleClass_ProbStr[0]);
//							String TopkTag = (intTopk - 1) + "";
//							Point unreasonPos = classToPosMap.getClassToPosMap().get(TopkTag);
//							
//							//与可靠点距离小于一定阈值，视为正确的label
//							if (reasonPos.isCloseEnough(unreasonPos, CloseDistance)) {
//								classlist[i] = TopkTag;
//								break;
//							}
//						}
//						
//						//Top3/2的距离与可靠点都很远
//						if (j == singleClassProbStr.length) {
//							
//						}
//					}
//				}
//			}
//			else if (reasonableCount == 2) {    //2个，找不可靠的那个的top2/top3，看谁靠近，则视为正确
//				int unreasonIndex;    //不可靠的top1
//				for (int i = 0; i < reasonableTopOne.length; i++) {
//					if (!reasonableTopOne[i]) {
//						unreasonIndex = i;
//						break;
//					}
//				}
//				
//				
//			}
//			else {    //3个，分类tag直接赋值
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
		else {    //上传图片无法找到匹配类别，设置类别为-1，给客户端提示
			for (int i = 0; i < classlist.length; i++) {
				classlist[i] = "-1";
			}
		}
		
		clientSocket.close();
	}
	
	/**
	 * 返回classlist
	 */
	public String[] getClasslist() {
		return last_classlist;
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
			
			last_classlist[i] = classlist[i];
			classlist[i] = "";
		}
		return shopPosSet;
	}
}
