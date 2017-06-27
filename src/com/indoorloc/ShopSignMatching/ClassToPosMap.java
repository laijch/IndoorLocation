package com.indoorloc.ShopSignMatching;

import java.util.HashMap;
import java.util.Map;

import com.indoorloc.model.Point;

/**
 * 招牌类别与地理位置映射表
 * @author Mo Haoran
 */
public class ClassToPosMap {
	private Map<String, Point> classToPosMap = new HashMap<String, Point>();
	
	public ClassToPosMap() {
		// 1~10
		classToPosMap.put("1", new Point(2423,641));
		classToPosMap.put("2", new Point(2357,641));
		classToPosMap.put("3", new Point(2287,645));
		classToPosMap.put("4", new Point(2069,643));
		classToPosMap.put("5", new Point(2003,645));
		classToPosMap.put("6", new Point(1935,643));
		classToPosMap.put("7", new Point(1799,653));
		classToPosMap.put("8", new Point(1735,649));
		classToPosMap.put("9", new Point(1669,649));
		classToPosMap.put("10", new Point(1599,645));
		// 11~20
		classToPosMap.put("11", new Point(1543,635));
		classToPosMap.put("12", new Point(1491,611));
		classToPosMap.put("13", new Point(1451,587));
		classToPosMap.put("14", new Point(1409,591));
		classToPosMap.put("15", new Point(1363,593));
		classToPosMap.put("16", new Point(1321,589));
		classToPosMap.put("17", new Point(1255,629));
		classToPosMap.put("18", new Point(1209,711));
		classToPosMap.put("19", new Point(1343,811));
		classToPosMap.put("20", new Point(1344,895));
		// 21——30
		classToPosMap.put("21", new Point(1343,965));
		classToPosMap.put("22", new Point(1229,999));
		classToPosMap.put("23", new Point(1130,1000));
		classToPosMap.put("24", new Point(1069,1001));
		classToPosMap.put("25", new Point(991,997));
		classToPosMap.put("26", new Point(925,999));
		classToPosMap.put("27", new Point(859,999));
		classToPosMap.put("28", new Point(783,997));
		classToPosMap.put("29", new Point(609,951));
		classToPosMap.put("30", new Point(430,951));
		// 31~40
		classToPosMap.put("31", new Point(455,875));
		classToPosMap.put("32", new Point(526,873));
		classToPosMap.put("33", new Point(856,873));
		classToPosMap.put("34", new Point(920,873));
		classToPosMap.put("35", new Point(990,871));
		classToPosMap.put("36", new Point(1058,873));
		classToPosMap.put("37", new Point(1126,871));
		classToPosMap.put("38", new Point(1122,727));
		classToPosMap.put("39", new Point(1060,731));
		classToPosMap.put("40", new Point(990,727));
		// 41~50
		classToPosMap.put("41", new Point(894,729));
		classToPosMap.put("42", new Point(522,727));
		classToPosMap.put("43", new Point(460,733));
		classToPosMap.put("44", new Point(462,673));
		classToPosMap.put("45", new Point(522,669));
		classToPosMap.put("46", new Point(890,667));
		classToPosMap.put("47", new Point(962,665));
		classToPosMap.put("48", new Point(1036,671));
		classToPosMap.put("49", new Point(1092,563));
		classToPosMap.put("50", new Point(1094,507));
		// 51~60
		classToPosMap.put("51", new Point(824,501));
		classToPosMap.put("52", new Point(824,561));
		classToPosMap.put("53", new Point(524,461));
		classToPosMap.put("54", new Point(464,460));
		classToPosMap.put("55", new Point(405,338));
		classToPosMap.put("56", new Point(460,335));
		classToPosMap.put("57", new Point(528,336));
		classToPosMap.put("58", new Point(595,340));
		classToPosMap.put("59", new Point(704,332));
		classToPosMap.put("60", new Point(896,396));
		// 61~?
		classToPosMap.put("61", new Point(1120,394));
		classToPosMap.put("62", new Point(1586,394));
	}
	
	public Map<String, Point> getClassToPosMap() {
		return classToPosMap;
	}
}
