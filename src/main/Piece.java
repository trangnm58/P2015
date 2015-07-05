// Piece.java

package main;
import java.util.*;

public class Piece {
	private Block[] body;
	
	// topLeft and botRight are just points in 8x8 area, don't need to be blocks
	private Point topLeft;
	private Point botRight;
	
	public Piece() {
	}
	
	/*
	 * constructor function from array of Point
	 */
	public Piece(Block[] blocks) {
		body = new Block[blocks.length];
		for (int i=0; i < blocks.length; i++) {
			body[i] = blocks[i];
		}
		// sort body
		sortBody();
		findTopLeftBotRight();
	}

	public Block[] getBody() {
		return body;
	}
	
	/*
	 * rotate function
	 * rotate the piece 90* clockwise
	 * return new Piece
	 */
	public Piece rotate() {
		Block[] newBody = new Block[body.length];
		
		for (int i=0; i < body.length; i++) {
			newBody[i].y = body[i].x;
			newBody[i].x = 7 - body[i].y;
		}
		
		return new Piece(newBody);
	}
	
	/*
	 * flip function
	 * return new Piece
	 */
	public Piece flip() {
		Block[] newBody = new Block[body.length];
		
		for (int i=0; i < body.length; i++) {
			newBody[i].x = (body[i].x + 7) % 8;
			newBody[i].y = body[i].y;
		}
		
		return new Piece(newBody);
	}
	
	private void findTopLeftBotRight() {
		int maxX, maxY, minX, minY;
		maxX = maxY = minX = minY = 0;
		
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
