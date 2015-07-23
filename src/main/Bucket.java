// Bucket.java

package main;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Bucket {
	public HashMap<Integer, Piece> pieces;
	public int minSize;
	public PriorityQueue<Integer> idQueue;
	public PriorityQueue<Integer> sizeQueue;

	public Bucket() {
		pieces = new HashMap<Integer, Piece>();
		minSize = 17;
		idQueue = new PriorityQueue<Integer>();
		sizeQueue = new PriorityQueue<Integer>();
		idQueue.add(1030);
		sizeQueue.add(17);
	}

	/*
	 * push piece to pieces
	 */
	public boolean push (Piece piece) {
		pieces.put(piece.id, piece);
		idQueue.add(piece.id);
		sizeQueue.add(piece.body.length);
		minSize = sizeQueue.peek();
		return true;
	}
	
	/*
	 * pop the piece with the smallest id
	 */
	public Piece pop() {
		if (pieces.size() != 0) {
			Piece p = get(idQueue.peek());	
			return p;
		} else {
			return null;
		}
	}
	
	/*
	 * get piece by id
	 */
	public Piece get (int id) {
		Piece piece = pieces.get(id);
		pieces.remove(id);
		if (piece != null) {
			idQueue.remove(piece.id);
			sizeQueue.remove(piece.body.length);
			minSize = sizeQueue.peek();
		}
		return piece;
	}
}
