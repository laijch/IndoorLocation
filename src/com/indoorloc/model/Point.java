package com.indoorloc.model;

public class Point {
	public int x;
	public int y;

	public Point(int _x, int _y) {
        x = _x;
        y = _y;
    }
	
	public boolean equal(Point p) {
		return (x == p.x && y == p.y);
	}
	
	public boolean isCloseEnough(Point p, double distance) {
		return ((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y)) < distance;
	}
}
