// Point.java

package main;

public class Point {
	public int x;
	public int y;
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this(p.x, p.y);
	}
	
	/*
	 * p1 smaller than p2 if y1 < y2 or their ys are equal but x1 < x2 
	 */
	public boolean isSmaller(Point p) {
		if (y < p.y) return true;
		else if (y > p.y) return false;
		else {
			if (x < p.x) return true;
			else return false;
		}
	}
	
	public boolean equals(Object other) {
		// standard two checks for equals()
		if (this == other) return true;
		if (!(other instanceof Point)) return false;

		// check if other point is the same
		Point pt = (Point)other;
		return(x == pt.x && y == pt.y);
	}
	
	public Point add(Point p) {
		return new Point(p.x + x, p.y + y);
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
