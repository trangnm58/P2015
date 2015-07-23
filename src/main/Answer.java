// Answer.java

package main;

import java.util.HashMap;

public class Answer {
	public HashMap<Integer, Line> lines = new HashMap<Integer, Line>();

	public Answer() {}
	
	/*
	 * constructor with Board
	 */
	public Answer(Board board) {
		while(!board.pieceStack.isEmpty()){
			push(board.posStack.pop(), board.pieceStack.pop());
		}
	}
	
	public void push (Point ref, Piece p) {
		Line line = new Line(ref, p);
		lines.put(p.id, line);
	}
}

class Line {
	public Point ref;
	public String side;
	public int angle;
	
	public Line (Point ref, Piece p) {
		this.ref = ref;
		if (p.up) {
			this.side = "H";
		} else {
			this.side = "T";
		}
		this.angle = p.angle;
	}
}