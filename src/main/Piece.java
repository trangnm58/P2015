// Piece.java

package main;

public class Piece {
	private Block[] body;
	
	// topLeft and botRight are just points in 8x8 area, don't need to be blocks
	public Point topLeft;
	public Point botRight;
	
	public Piece() {}
	
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
		int x, y;
		for (int i=0; i < body.length; i++) {
			y = body[i].x;
			x = 7 - body[i].y;
			newBody[i] = new Block(x, y);
		}
		
		return new Piece(newBody);
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
		
		return new Piece(newBody);
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
