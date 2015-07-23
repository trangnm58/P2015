// Piece.java

package main;

import java.util.Stack;
import java.util.ArrayList;

public class Piece {
	public Block[] body;
	
	// topLeft and botRight are just points in 8x8 area, don't need to be blocks
	public Point topLeft;
	public Point botRight;
	
	// status variables
	public boolean up; // true => "H", false => "T"
	public int angle;	// 0 / 90 / 180 / 270
	public int id;
	
	public Piece() {}
	
	/*
	 * constructor function from array of Point
	 */
	public Piece(Block[] blocks, int id) {
		body = new Block[blocks.length];
		for (int i=0; i < blocks.length; i++) {
			body[i] = blocks[i];
		}
		
		this.up = true;
		this.angle = 0;
		this.id = id;
		
		if (body.length > 0) {
			// sort body
			sortBody();
			findTopLeftBotRight();
		}
	}
	
	public Piece(Block[] blocks, boolean up, int angle, int id) {
		this(blocks, id);
		this.up = up;
		this.angle = angle;
	}
	
	/*
	 * copy constructor
	 */
	public Piece(Piece other) {
		this(other.body, other.up, other.angle, other.id);
	}

	/*
	 * compare two Piece
	 * true if two bodies are equal
	 */
	public boolean equals (Piece other) {
		if (this == other) return true;
		if (this.body.length != other.body.length) 
			return false;
		for (int i=0; i < this.body.length; i++) {
			if (!this.body[i].equals(other.body[i])) {
				return false;
			}
		}
		return true;
	}

	/*
	 * compare two equivalent Pieces
	 */
	public boolean equiv(Piece other) {
		// equals => equivalent
		if (equals(other)) return true;

		// if body.length are different
		if (body.length != other.body.length) return false;

		// if the contain areas are not the same
		int w1, w2, h1, h2;
		w1 = botRight.x - topLeft.x;
		h1 = botRight.y - topLeft.y;
		w2 = other.botRight.x - other.topLeft.x;
		h2 = other.botRight.y - other.topLeft.y;
		if (w1 != w2 || h1 != h2) return false;

		// new body has ref = topLeft
		Block[] body1 = new Block[body.length];
		Block[] body2 = new Block[body.length];
		// change body1 and body2
		for (int i=0; i < body.length; i++) {
			body1[i] = new Block(body[i].x - topLeft.x, body[i].y - topLeft.y);
			body2[i] = new Block(other.body[i].x - other.topLeft.x, other.body[i].y - other.topLeft.y);
		}
		Piece p1 = new Piece();
		Piece p2 = new Piece();
		p1.body = body1;
		p2.body = body2;
		
		if (p1.equals(p2)) return true;
		
		return false;
	}
	
	/*
	 * rotate function
	 * rotate the piece 90* clockwise
	 * return new Piece
	 */
	public Piece rotate() {
		Block[] newBody = new Block[body.length];
		int x, y;
		for (int i=0; i < body.length; i++) {
			y = body[i].x;
			x = 7 - body[i].y;
			newBody[i] = new Block(x, y);
		}
		
		int newAngle = (this.angle + 90) % 360;
		
		return new Piece(newBody, this.up, newAngle, this.id);
	}
	
	/*
	 * flip function
	 * return new Piece
	 */
	public Piece flip() {
		Block[] newBody = new Block[body.length];
		int x, y;
		for (int i=0; i < body.length; i++) {
			x = 7 - body[i].x;
			y = body[i].y;
			newBody[i] = new Block(x, y);
		}
		
		return new Piece(newBody, !this.up, angle, this.id);
	}
	
	public Stack<Piece> parsePiece() {
		// find all status and push in pieces
		// order: rotate => flip => rotate of flip
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		
		// add all rotate pieces if different
		Piece p = rotate();
		while (!equiv(p)) {
			pieces.add(p);
			p = p.rotate();
		}
		// add flip piece if different from original and all pieces in "pieces"
		p = flip();
		if (!equiv(p)) {
			boolean flag = false;
			for (int i=0; i < pieces.size(); i++) {
				// if flip piece equals 1 piece in pieces
				if (p.equiv(pieces.get(i))) {
					flag = true;
				}
			}
			if (!flag) pieces.add(p);
		}
		// add all rotate pieces of flip piece
		Piece p2 = p.rotate();	// p is now flip piece
		if (!equiv(p2)) {
			while (!p.equiv(p2)) {
				boolean flag = false;
				for (int i = 0; i < pieces.size(); i++) {
					// if flip piece equals 1 piece in pieces
					if (p2.equiv(pieces.get(i))) {
						flag = true;
					}
				}
				if (!flag) pieces.add(p2);
				p2 = p2.rotate();
			}
		}
		
		// convert to Stack
		Stack<Piece> temp = new Stack<Piece>();
		while (pieces.size() > 0) {
			temp.push(pieces.remove(0));
		}
		return temp;
	}
	
	/*
	 * find the top left point and the bottom right point
	 */
	private void findTopLeftBotRight() {
		int maxX, maxY, minX, minY;
		maxX = minX = body[0].x;
		maxY = minY = body[0].y;
		
		for (int i=0; i < body.length; i++) {
			if (body[i].x > maxX) {
				maxX = body[i].x;
			}
			if (body[i].x < minX) {
				minX = body[i].x;
			}
			if (body[i].y > maxY) {
				maxY = body[i].y;
			}
			if (body[i].y < minY)	{
				minY = body[i].y;
			}
		}
		
		topLeft = new Point(minX, minY);
		botRight = new Point(maxX, maxY);
	}
	
	/*
	 * sort all points in order: left to right, top to bottom
	 * insertion sort algorithm
	 */
	private void sortBody() {
		int pos;
		for (int i=1; i < body.length; i++) {
			pos = i - 1;
			while (pos >= 0 && body[i].isSmaller(body[pos])) {
				pos--;
			}
			pos++;
			Block temp = new Block(body[i]);
			for (int j=i-1; j >= pos; j--) {
				body[j+1] = new Block(body[j]);
			}
			body[pos] = temp;
		}
	}
}
