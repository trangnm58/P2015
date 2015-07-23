// Bucket.java

package main;

import java.util.HashMap;

public class Bucket {
	public HashMap<Integer, Piece> pieces = new HashMap<Integer, Piece>();
	public Piece minSize;
	private int min = 17; // > max size of a piece

	/*
	 * push piece to pieces
	 */
	public boolean push (Piece piece) {
		pieces.put(piece.id, piece);
		if (piece.body.length < min) {
			min = piece.body.length;
			minSize = piece;
		}
		return true;
	}
	
	/*
	 * get piece by id
	 */
	public Piece get (int id) {
		Piece piece = pieces.get(id);
		pieces.remove(id);
		return piece;
	}
}
