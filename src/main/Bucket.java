// Bucket.java

package main;

import java.util.HashMap;

public class Bucket {
	public HashMap<Integer, Piece> pieces = new HashMap<Integer, Piece>();
	public int minSize = 17; // > max size of a piece
	private int minId = 1030;
	/*
	 * push piece to pieces
	 */
	public boolean push (Piece piece) {
		pieces.put(piece.id, piece);
		if (piece.body.length < minSize) {
			minSize = piece.body.length;
		}
		if (piece.id < minId) minId = piece.id;
		return true;
	}
	
	/*
	 * pop the piece with the smallest id
	 */
	public Piece pop() {
		Piece p = get(minId);
		
		// update minId
		Piece next = pieces.get(++minId);
		while (next == null) {
			next = pieces.get(++minId);
		}
		return p;
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
