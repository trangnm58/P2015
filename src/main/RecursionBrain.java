// RecursionBrain.java

package main;

import java.util.Random;
import java.util.Vector;

public class RecursionBrain extends Brain {
	private int minPoint;
	private int minPiece;
	public static final int MAX_SKIP = 150;
	private static final boolean ACCEPT_HOLE = true; 

	public RecursionBrain(Board board, Bucket bucket) {
		super(board, bucket);

		this.minPoint = 1024;
		this.minPiece = 1024;
	}

	public void think() {
		Board cBoard = (Board) this.board.clone();
		this.think(cBoard, this.bucket);
	}

	public void think(Board board, Bucket bucket) {
		if (bucket.pieces.size() == 0 || board.empty == 0
				|| board.empty < bucket.minSize) {
			if (board.empty < this.minPoint || board.empty == this.minPoint
					&& board.putted < this.minPiece) {
				this.minPoint = board.empty;
				this.minPiece = board.putted;

				this.answer = new Answer(board);
				// write to file
				OStream.writeToFile(this.answer, this.minPoint);
			}
			// else do nothing
		} else if (board.empty - bucket.totalBlock < Math.min(minPoint, MAX_SKIP)) {
			Piece newPiece = bucket.pop();
			// get all state of this piece
			Vector<Piece> allState = newPiece.parsePiece();
			// create a random generator
			Random randomGenerator = new Random();
			

			int randomUse = randomGenerator.nextInt(200);
			if (randomUse >= 20) { // not use after use
				// in case of use the piece, ask the board for all
				// available point to put this state
				while (!allState.isEmpty()) {
					int randomState = randomGenerator.nextInt(allState.size());
					Piece aState = allState.remove(randomState);
					Vector<Point> allPosition = board.where(aState);

					while (!allPosition.isEmpty()) {
						// put random in state and continue to think
						int randomInt = randomGenerator.nextInt(allPosition.size());
						Point temp = allPosition.remove(randomInt);

						Board cBoard = (Board) board.clone();
						cBoard.put(aState, temp);
						int lookHole = cBoard.look();
						if (lookHole > bucket.minSize || board.empty == lookHole || ACCEPT_HOLE) {
							this.think(cBoard, bucket);
						} else {
							cBoard.undo();
						}
					}
				}
				// in case of not use this piece, continue to think
				Board cBoard = (Board) board.clone();
				this.think(cBoard, bucket);
			} else { // use after not use
				// in case of not use this piece, continue to think
				Board cBoard = (Board) board.clone();
				this.think(cBoard, bucket);
				// in case of use the piece, ask the board for all
				// available point to put this state
				while (!allState.isEmpty()) {
					int randomState = randomGenerator.nextInt(allState.size());
					Piece aState = allState.remove(randomState);
					Vector<Point> allPosition = board.where(aState);

					while (!allPosition.isEmpty()) {
						// put random in state and continue to think
						int randomInt = randomGenerator.nextInt(allPosition.size());
						Point temp = allPosition.remove(randomInt);

						cBoard = (Board) board.clone();
						cBoard.put(aState, temp);
						int lookHole = cBoard.look();
						if (lookHole > bucket.minSize || board.empty == lookHole || ACCEPT_HOLE) {
							this.think(cBoard, bucket);
						} else {
							cBoard.undo();
						}
					}
				}
			}
			bucket.push(newPiece);
		}
	}
}
