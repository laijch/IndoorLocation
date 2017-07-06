package com.indoorloc.ShopSignMatching;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.indoorloc.model.Point;

import javafx.util.Pair;

/**
 * ѵ����ǩ�����λ��ӳ���
 * @author Mo Haoran
 * @author Lai Jiacheng
 */
public class ClassToPosMap {
	// classToPosMap(ѵ����ǩ,����)
	private Map<String, Point> classToPosMap = new HashMap<String, Point>();
	
	public ClassToPosMap() {
		classToPosMap = readShopData();
	}
	
	public Map<String, Point> getClassToPosMap() {
		return classToPosMap;
	}
	
	// �����ļ�
	public static Map<String, Point> readShopData() {
		
		// dataMap(ѵ����ǩ,����)
        Map<String, Point> dataMap = new HashMap<String, Point>();
        // labelMap(��ͼ���,ѵ����ǩ)
        Map<String, String> labelMap = new HashMap<String, String>();
		
        String tmp = readTxtFile("http://localhost:8080/IndoorLocServer/map.txt");
        String[] data = tmp.split("#");
        // i=0������һ�У���������Ϣ
        for (int i = 1; i < data.length; ++i) {
            String[] s = data[i].split(",");
            labelMap.put(s[1], s[0]);
        }

        tmp = readTxtFile("http://localhost:8080/IndoorLocServer/location.txt");
        data = tmp.split("#");
        for (int i = 1; i < data.length; ++i) {
            String[] s = data[i].split(",");
            if (labelMap.get(s[0]) == null) continue;
            int x = Integer.parseInt(s[1].substring(1));
            int y = Integer.parseInt(s[2].substring(0, s[2].length()-1));
            String label = labelMap.get(s[0]); 
            dataMap.put(label, new Point(x, y));
        }

        return dataMap;
    }
	// ��ȡ�ļ�
    private static String readTxtFile(String filePath) {
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
        	File file=new File(filePath);
        	InputStreamReader read = new InputStreamReader(
        	                    new FileInputStream(file),"utf-8"); // ���ǵ������ʽ
            bufferedReader = new BufferedReader(read);
            String line = null;
            // ÿһ����#����(��β��#)
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line+"#");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

}
