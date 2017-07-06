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
 * 训练标签与地理位置映射表
 * @author Mo Haoran
 * @author Lai Jiacheng
 */
public class ClassToPosMap {
	// classToPosMap(训练标签,坐标)
	private Map<String, Point> classToPosMap = new HashMap<String, Point>();
	
	public ClassToPosMap() {
		classToPosMap = readShopData();
	}
	
	public Map<String, Point> getClassToPosMap() {
		return classToPosMap;
	}
	
	// 解析文件
	public static Map<String, Point> readShopData() {
		
		// dataMap(训练标签,坐标)
        Map<String, Point> dataMap = new HashMap<String, Point>();
        // labelMap(地图序号,训练标签)
        Map<String, String> labelMap = new HashMap<String, String>();
		
        String tmp = readTxtFile("http://localhost:8080/IndoorLocServer/map.txt");
        String[] data = tmp.split("#");
        // i=0，即第一行，是无用信息
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
	// 读取文件
    private static String readTxtFile(String filePath) {
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
        	File file=new File(filePath);
        	InputStreamReader read = new InputStreamReader(
        	                    new FileInputStream(file),"utf-8"); // 考虑到编码格式
            bufferedReader = new BufferedReader(read);
            String line = null;
            // 每一行用#隔开(行尾加#)
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line+"#");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

}
