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
}
